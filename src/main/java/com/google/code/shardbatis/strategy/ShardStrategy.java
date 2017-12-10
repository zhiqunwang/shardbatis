package com.google.code.shardbatis.strategy;

public abstract interface ShardStrategy
{
  public abstract String getTargetTableName(String paramString1, Object paramObject, String paramString2);
}

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.strategy.ShardStrategy
 * JD-Core Version:    0.6.2
 */