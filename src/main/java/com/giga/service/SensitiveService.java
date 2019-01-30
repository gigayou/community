package com.giga.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

	// 敏感词替换
	private static final String DEFAULT_REPLACEMENT = "******";

	private class TrieNode {

		// true: 关键词末尾flag
		private boolean end;

		// subNode
		private Map<Character, TrieNode> subNode = new HashMap<>();

		// 在指定位置添加节点树
		void addSubNode(Character key, TrieNode node) {
			subNode.put(key, node);
		}

		// 获得节点树
		TrieNode getSubNode(Character key) {
			return subNode.get(key);
		}

		// 判断是否关键词末尾
		boolean isKeyWordEnd() {
			return end;
		}

		// 设置关键词末尾flag
		void setKeyWordEnd(boolean end) {
			this.end = end;
		}

		// 获得节点树长度
		int getSubNoteCount() {
			return subNode.size();
		}
	}

	// 根节点
	private TrieNode rootNode = new TrieNode();

	// 特殊符号判断
	private boolean isSymbol(char c) {
		int ic = (int) c;
		// 0x2E80-0x9FFF 东亚文字范围
		return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
	}

	// 敏感词过滤器
	public String filter(String txt) {
		// 判断当前文本是否为空字符串
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		String replacement = DEFAULT_REPLACEMENT;
		StringBuilder result = new StringBuilder();

		int begin = 0;
		int position = 0;
		// 定义当前节点
		TrieNode curNode = rootNode;

		while (position < txt.length()) {
			char c = txt.charAt(position);
			// 判断c是否为特殊符号字符
			if (isSymbol(c)) {
				// 如果当前节点是根节点
				if (curNode == rootNode) {
					result.append(c);
					++begin;
				}
				++position;
				continue;
			}

			// 根据c获取子节点
			curNode = curNode.getSubNode(c);

			// 判断当前范围的敏感词匹配是否结束
			if (curNode == null) {
				// txt中位于begin位置的字符不属于敏感词
				result.append(c);
				// 开始过滤下一个字符
				position = begin + 1;
				begin = position;
				// 当前节点回到根节点
				curNode = rootNode;
			} else if (curNode.isKeyWordEnd()) {
				// txt中位于begin-position区间的字符串属于敏感词
				result.append(replacement);
				// 开始过滤下一个字符
				position = position + 1;
				begin = position;
				// 当前节点回到根节点
				curNode = rootNode;
			} else {
				++position;
			}
		}

		result.append(txt.substring(begin));
		return result.toString();
	}

	private void addWord(String txt) {
		// 定义当前节点指向根节点
		TrieNode curNode = rootNode;

		for (int index = 0; index < txt.length(); index++) {
			char c = txt.charAt(index);

			// 判断当前字符是否是特殊字符
			if (isSymbol(c)) {
				continue;
			}

			// 获取根节点
			TrieNode subNode = curNode.getSubNode(c);
			// 判断c字符对象是否有根节点
			if (subNode == null) {
				subNode = new TrieNode();
				curNode.addSubNode(c, subNode);
			}

			curNode = subNode;

			// 判断是否到达敏感词尾部
			if (index == txt.length() - 1) {
				subNode.setKeyWordEnd(true);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		rootNode = new TrieNode();

		try {
			InputStream is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("SensitiveWords.txt");
			InputStreamReader read = new InputStreamReader(is);
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				lineTxt = lineTxt.trim();
				addWord(lineTxt);
			}
			read.close();
		} catch (Exception e) {
			logger.error("读取敏感词文件失败" + e.getMessage());
		}
	}
}
