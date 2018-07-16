package com.linecy.module.decoration;

/**
 * 分组信息
 *
 * @author by linecy.
 */
public class GroupInfo {

  public GroupInfo(String key, int length) {
    this.key = key;
    this.length = length;
  }

  public final String key;//分组key
  public final int length;//分组长度
}
