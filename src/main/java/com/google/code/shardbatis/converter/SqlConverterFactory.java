/*    */ package com.google.code.shardbatis.converter;
/*    */ 
/*    */ import com.google.code.shardbatis.ShardException;
/*    */ import java.io.StringReader;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.sf.jsqlparser.JSQLParserException;
/*    */ import net.sf.jsqlparser.parser.CCJSqlParserManager;
/*    */ import net.sf.jsqlparser.statement.Statement;
/*    */ import net.sf.jsqlparser.statement.delete.Delete;
/*    */ import net.sf.jsqlparser.statement.insert.Insert;
/*    */ import net.sf.jsqlparser.statement.select.Select;
/*    */ import net.sf.jsqlparser.statement.update.Update;
/*    */ import org.apache.ibatis.logging.Log;
/*    */ import org.apache.ibatis.logging.LogFactory;
/*    */ 
/*    */ public class SqlConverterFactory
/*    */ {
/* 29 */   private static final Log log = LogFactory.getLog(SqlConverterFactory.class);
/*    */ 
/* 33 */   private static SqlConverterFactory factory = new SqlConverterFactory();
/*    */   private Map<String, SqlConverter> converterMap;
/*    */   private CCJSqlParserManager pm;
/*    */ 
/*    */   public static SqlConverterFactory getInstance()
/*    */   {
/* 37 */     return factory;
/*    */   }
/*    */ 
/*    */   private SqlConverterFactory()
/*    */   {
/* 44 */     this.converterMap = new HashMap();
/* 45 */     this.pm = new CCJSqlParserManager();
/* 46 */     register();
/*    */   }
/*    */ 
/*    */   private void register() {
/* 50 */     this.converterMap.put(Select.class.getName(), new SelectSqlConverter());
/* 51 */     this.converterMap.put(Insert.class.getName(), new InsertSqlConverter());
/* 52 */     this.converterMap.put(Update.class.getName(), new UpdateSqlConverter());
/* 53 */     this.converterMap.put(Delete.class.getName(), new DeleteSqlConverter());
/*    */   }
/*    */ 
/*    */   public String convert(String sql, Object params, String mapperId)
/*    */     throws ShardException
/*    */   {
/* 66 */     Statement statement = null;
/*    */     try {
/* 68 */       statement = this.pm.parse(new StringReader(sql));
/*    */     } catch (JSQLParserException e) {
/* 70 */       log.error(e.getMessage(), e);
/* 71 */       throw new ShardException(e);
/*    */     }
/*    */ 
/* 74 */     SqlConverter converter = (SqlConverter)this.converterMap.get(statement.getClass().getName());
/*    */ 
/* 77 */     if (converter != null) {
/* 78 */       return converter.convert(statement, params, mapperId);
/*    */     }
/* 80 */     return sql;
/*    */   }
/*    */ }

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.converter.SqlConverterFactory
 * JD-Core Version:    0.6.2
 */