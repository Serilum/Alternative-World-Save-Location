package com.natamus.alternativeworldsavelocation.util;

import java.io.File;
import java.util.regex.Matcher;

public class Util {
	public static String returnSystemSpecificPath(String path) {
		return path.replace("\\\\", "\\").replaceAll("(\\+|/+)", Matcher.quoteReplacement(File.separator));
	}
}
