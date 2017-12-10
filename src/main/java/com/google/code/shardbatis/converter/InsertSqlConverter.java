/*    */ package com.google.code.shardbatis.converter;
/*    */ 
/*    */ import net.sf.jsqlparser.schema.Table;
/*    */ import net.sf.jsqlparser.statement.Statement;
/*    */ import net.sf.jsqlparser.statement.insert.Insert;
/*    */ 
/*    */ public class InsertSqlConverter extends AbstractSqlConverter
/*    */ {
/*    */   protected Statement doConvert(Statement statement, Object params, String mapperId)
/*    */   {
/* 25 */     if (!(statement instanceof Insert)) {
/* 26 */       throw new IllegalArgumentException("The argument statement must is instance of Insert.");
/*    */     }
/*    */ 
/* 29 */     Insert insert = (Insert)statement;
/*    */ 
/* 31 */     String name = insert.getTable().getName();
/* 32 */     insert.getTable().setName(convertTableName(name, params, mapperId));
/*    */ 
/* 34 */     return insert;
/*    */   }
/*    */ }

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.converter.InsertSqlConverter
 * JD-Core Version:    0.6.2
 */