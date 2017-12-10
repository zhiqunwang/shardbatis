/*    */ package com.google.code.shardbatis.strategy.impl;
/*    */ 
/*    */ import com.google.code.shardbatis.strategy.ShardStrategy;
/*    */ 
/*    */ public class NoShardStrategy
/*    */   implements ShardStrategy
/*    */ {
/*    */   public String getTargetTableName(String baseTableName, Object params, String mapperId)
/*    */   {
/* 20 */     return baseTableName;
/*    */   }
/*    */ }

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.strategy.impl.NoShardStrategy
 * JD-Core Version:    0.6.2
 */