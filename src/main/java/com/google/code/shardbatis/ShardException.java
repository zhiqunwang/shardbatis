/*    */ package com.google.code.shardbatis;
/*    */ 
/*    */ public class ShardException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1793760050084714190L;
/*    */ 
/*    */   public ShardException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ShardException(String msg)
/*    */   {
/* 19 */     super(msg);
/*    */   }
/*    */ 
/*    */   public ShardException(String msg, Throwable t) {
/* 23 */     super(msg, t);
/*    */   }
/*    */ 
/*    */   public ShardException(Throwable t) {
/* 27 */     super(t);
/*    */   }
/*    */ }

