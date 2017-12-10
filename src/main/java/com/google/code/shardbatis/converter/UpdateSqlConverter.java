/*    */ package com.google.code.shardbatis.converter;
/*    */ 
/*    */ import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.update.Update;

/*    */
/*    */
/*    */ 
/*    */ public class UpdateSqlConverter extends AbstractSqlConverter
/*    */ {
/*    */   protected Statement doConvert(Statement statement, Object params, String mapperId)
/*    */   {
/* 25 */     if (!(statement instanceof Update)) {
/* 26 */       throw new IllegalArgumentException("The argument statement must is instance of Update.");
/*    */     }
/*    */ 
/* 29 */     Update update = (Update)statement;
/* 30 */     String name = update.getTable().getName();
/* 31 */     update.getTable().setName(convertTableName(name, params, mapperId));
/*    */ 
/* 33 */     return update;
/*    */   }
/*    */ }

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.converter.UpdateSqlConverter
 * JD-Core Version:    0.6.2
 */