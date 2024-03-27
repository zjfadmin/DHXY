package org.come.action.qiandao;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;

import java.nio.charset.Charset;

public class Base64 {
  public static byte[] encode(byte[] arr, boolean lineSep) {
    return Base64Encoder.encode(arr, lineSep);
  }
  
  public static byte[] encodeUrlSafe(byte[] arr, boolean lineSep) {
    return Base64Encoder.encodeUrlSafe(arr, lineSep);
  }
  
  public static String encode(String source) {
    return Base64Encoder.encode(source);
  }
  
  public static String encodeUrlSafe(String source) {
    return Base64Encoder.encodeUrlSafe(source);
  }
  
  public static String encode(String source, String charset) {
    return Base64Encoder.encode(source);
  }
  
  public static String encodeUrlSafe(String source, String charset) {
    return Base64Encoder.encodeUrlSafe(source);
  }
  
  public static String encode(String source, Charset charset) {
    return Base64Encoder.encode(source, charset);
  }
  
  public static String encodeUrlSafe(String source, Charset charset) {
    return Base64Encoder.encodeUrlSafe(source, charset);
  }
  
  public static String encode(byte[] source) {
    return Base64Encoder.encode(source);
  }
  
  public static String encodeUrlSafe(byte[] source) {
    return Base64Encoder.encodeUrlSafe(source);
  }
  
  public static String encode(byte[] source, String charset) {
    return Base64Encoder.encode(source);
  }
  
  public static String encodeUrlSafe(byte[] source, String charset) {
    return Base64Encoder.encodeUrlSafe(source);
  }
  
  public static String encode(byte[] source, Charset charset) {
    return Base64Encoder.encode(source);
  }
  
  public static String encodeUrlSafe(byte[] source, Charset charset) {
    return Base64Encoder.encodeUrlSafe(source);
  }
  
  public static byte[] encode(byte[] arr, boolean isMultiLine, boolean isUrlSafe) {
    return Base64Encoder.encode(arr, isMultiLine, isUrlSafe);
  }
  
  public static String decodeStr(String source) {
    return Base64Decoder.decodeStr(source);
  }
  
  public static String decodeStr(String source, String charset) {
    return Base64Decoder.decodeStr(source);
  }
  
  public static String decodeStr(String source, Charset charset) {
    return Base64Decoder.decodeStr(source, charset);
  }
  
  public static byte[] decode(String source) {
    return Base64Decoder.decode(source);
  }
  
  public static byte[] decode(String source, String charset) {
    return Base64Decoder.decode(source);
  }
  
  public static byte[] decode(String source, Charset charset) {
    return Base64Decoder.decode(source);
  }
  
  public static byte[] decode(byte[] in) {
    return Base64Decoder.decode(in);
  }
}
