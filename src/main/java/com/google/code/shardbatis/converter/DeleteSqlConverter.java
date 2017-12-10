/*    */ package com.google.code.shardbatis.converter;
/*    */ 
/*    */ import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;

/*    */
/*    */
/*    */ 
/*    */ public class DeleteSqlConverter extends AbstractSqlConverter
/*    */ {
/*    */   protected Statement doConvert(Statement statement, Object params, String mapperId)
/*    */   {
/* 25 */     if (!(statement instanceof Delete)) {
/* 26 */       throw new IllegalArgumentException("The argument statement must is instance of Delete.");
/*    */     }
/*    */ 
/* 29 */     Delete delete = (Delete)statement;
/*    */ 
/* 31 */     String name = delete.getTable().getName();
/* 32 */     delete.getTable().setName(convertTableName(name, params, mapperId));
/*    */ 
/* 34 */     return delete;
/*    */   }
/*    */ }
