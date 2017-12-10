/*     */ package com.google.code.shardbatis.converter;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.sf.jsqlparser.expression.AllComparisonExpression;
/*     */ import net.sf.jsqlparser.expression.AnyComparisonExpression;
/*     */ import net.sf.jsqlparser.expression.BinaryExpression;
/*     */ import net.sf.jsqlparser.expression.CaseExpression;
/*     */ import net.sf.jsqlparser.expression.DateValue;
/*     */ import net.sf.jsqlparser.expression.DoubleValue;
/*     */ import net.sf.jsqlparser.expression.Expression;
/*     */ import net.sf.jsqlparser.expression.ExpressionVisitor;
/*     */ import net.sf.jsqlparser.expression.Function;
/*     */ import net.sf.jsqlparser.expression.InverseExpression;
/*     */ import net.sf.jsqlparser.expression.JdbcParameter;
/*     */ import net.sf.jsqlparser.expression.LongValue;
/*     */ import net.sf.jsqlparser.expression.NullValue;
/*     */ import net.sf.jsqlparser.expression.Parenthesis;
/*     */ import net.sf.jsqlparser.expression.StringValue;
/*     */ import net.sf.jsqlparser.expression.TimeValue;
/*     */ import net.sf.jsqlparser.expression.TimestampValue;
/*     */ import net.sf.jsqlparser.expression.WhenClause;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.Division;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
/*     */ import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
/*     */ import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
/*     */ import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
/*     */ import net.sf.jsqlparser.expression.operators.relational.Between;
/*     */ import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
/*     */ import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
/*     */ import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
/*     */ import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
/*     */ import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
/*     */ import net.sf.jsqlparser.expression.operators.relational.InExpression;
/*     */ import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
/*     */ import net.sf.jsqlparser.expression.operators.relational.ItemsList;
/*     */ import net.sf.jsqlparser.expression.operators.relational.ItemsListVisitor;
/*     */ import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
/*     */ import net.sf.jsqlparser.expression.operators.relational.Matches;
/*     */ import net.sf.jsqlparser.expression.operators.relational.MinorThan;
/*     */ import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
/*     */ import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
/*     */ import net.sf.jsqlparser.schema.Column;
/*     */ import net.sf.jsqlparser.schema.Table;
/*     */ import net.sf.jsqlparser.statement.Statement;
/*     */ import net.sf.jsqlparser.statement.select.FromItem;
/*     */ import net.sf.jsqlparser.statement.select.FromItemVisitor;
/*     */ import net.sf.jsqlparser.statement.select.Join;
/*     */ import net.sf.jsqlparser.statement.select.PlainSelect;
/*     */ import net.sf.jsqlparser.statement.select.Select;
/*     */ import net.sf.jsqlparser.statement.select.SelectBody;
/*     */ import net.sf.jsqlparser.statement.select.SelectVisitor;
/*     */ import net.sf.jsqlparser.statement.select.SubJoin;
/*     */ import net.sf.jsqlparser.statement.select.SubSelect;
/*     */ import net.sf.jsqlparser.statement.select.Union;
/*     */ 
/*     */ public class SelectSqlConverter extends AbstractSqlConverter
/*     */ {
/*     */   protected Statement doConvert(Statement statement, Object params, String mapperId)
/*     */   {
/*  78 */     if (!(statement instanceof Select)) {
/*  79 */       throw new IllegalArgumentException("The argument statement must is instance of Select.");
/*     */     }
/*     */ 
/*  82 */     TableNameModifier modifier = new TableNameModifier(params, mapperId);
/*  83 */     ((Select)statement).getSelectBody().accept(modifier);
/*  84 */     return statement;
/*     */   }
/*     */ 
/*     */   private class TableNameModifier implements SelectVisitor, FromItemVisitor, ExpressionVisitor, ItemsListVisitor {
/*     */     private Object params;
/*     */     private String mapperId;
/*     */ 
/*     */     TableNameModifier(Object params, String mapperId) {
/*  93 */       this.params = params;
/*  94 */       this.mapperId = mapperId;
/*     */     }
/*     */ 
/*     */     public void visit(PlainSelect plainSelect)
/*     */     {
/*  99 */       plainSelect.getFromItem().accept(this);
/*     */ 
/* 101 */       if (plainSelect.getJoins() != null) {
/* 102 */         Iterator joinsIt = plainSelect.getJoins().iterator();
/* 103 */         while (joinsIt.hasNext()) {
/* 104 */           Join join = (Join)joinsIt.next();
/* 105 */           join.getRightItem().accept(this);
/*     */         }
/*     */       }
/* 108 */       if (plainSelect.getWhere() != null)
/* 109 */         plainSelect.getWhere().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(Union union)
/*     */     {
/* 115 */       Iterator iter = union.getPlainSelects().iterator();
/* 116 */       while (iter.hasNext()) {
/* 117 */         PlainSelect plainSelect = (PlainSelect)iter.next();
/* 118 */         visit(plainSelect);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visit(Table tableName) {
/* 123 */       String table = tableName.getName();
/* 124 */       table = SelectSqlConverter.this.convertTableName(table, this.params, this.mapperId);
/*     */ 
/* 126 */       tableName.setName(table);
/*     */     }
/*     */ 
/*     */     public void visit(SubSelect subSelect) {
/* 130 */       subSelect.getSelectBody().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(Addition addition) {
/* 134 */       visitBinaryExpression(addition);
/*     */     }
/*     */ 
/*     */     public void visit(AndExpression andExpression) {
/* 138 */       visitBinaryExpression(andExpression);
/*     */     }
/*     */ 
/*     */     public void visit(Between between) {
/* 142 */       between.getLeftExpression().accept(this);
/* 143 */       between.getBetweenExpressionStart().accept(this);
/* 144 */       between.getBetweenExpressionEnd().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(Column tableColumn) {
/*     */     }
/*     */ 
/*     */     public void visit(Division division) {
/* 151 */       visitBinaryExpression(division);
/*     */     }
/*     */ 
/*     */     public void visit(DoubleValue doubleValue) {
/*     */     }
/*     */ 
/*     */     public void visit(EqualsTo equalsTo) {
/* 158 */       visitBinaryExpression(equalsTo);
/*     */     }
/*     */ 
/*     */     public void visit(Function function) {
/*     */     }
/*     */ 
/*     */     public void visit(GreaterThan greaterThan) {
/* 165 */       visitBinaryExpression(greaterThan);
/*     */     }
/*     */ 
/*     */     public void visit(GreaterThanEquals greaterThanEquals) {
/* 169 */       visitBinaryExpression(greaterThanEquals);
/*     */     }
/*     */ 
/*     */     public void visit(InExpression inExpression) {
/* 173 */       inExpression.getLeftExpression().accept(this);
/* 174 */       inExpression.getItemsList().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(InverseExpression inverseExpression) {
/* 178 */       inverseExpression.getExpression().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(IsNullExpression isNullExpression) {
/*     */     }
/*     */ 
/*     */     public void visit(JdbcParameter jdbcParameter) {
/*     */     }
/*     */ 
/*     */     public void visit(LikeExpression likeExpression) {
/* 188 */       visitBinaryExpression(likeExpression);
/*     */     }
/*     */ 
/*     */     public void visit(ExistsExpression existsExpression) {
/* 192 */       existsExpression.getRightExpression().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(LongValue longValue) {
/*     */     }
/*     */ 
/*     */     public void visit(MinorThan minorThan) {
/* 199 */       visitBinaryExpression(minorThan);
/*     */     }
/*     */ 
/*     */     public void visit(MinorThanEquals minorThanEquals) {
/* 203 */       visitBinaryExpression(minorThanEquals);
/*     */     }
/*     */ 
/*     */     public void visit(Multiplication multiplication) {
/* 207 */       visitBinaryExpression(multiplication);
/*     */     }
/*     */ 
/*     */     public void visit(NotEqualsTo notEqualsTo) {
/* 211 */       visitBinaryExpression(notEqualsTo);
/*     */     }
/*     */ 
/*     */     public void visit(NullValue nullValue) {
/*     */     }
/*     */ 
/*     */     public void visit(OrExpression orExpression) {
/* 218 */       visitBinaryExpression(orExpression);
/*     */     }
/*     */ 
/*     */     public void visit(Parenthesis parenthesis) {
/* 222 */       parenthesis.getExpression().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(Subtraction subtraction) {
/* 226 */       visitBinaryExpression(subtraction);
/*     */     }
/*     */ 
/*     */     public void visitBinaryExpression(BinaryExpression binaryExpression) {
/* 230 */       binaryExpression.getLeftExpression().accept(this);
/* 231 */       binaryExpression.getRightExpression().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(ExpressionList expressionList)
/*     */     {
/* 236 */       Iterator iter = expressionList.getExpressions().iterator();
/* 237 */       while (iter.hasNext()) {
/* 238 */         Expression expression = (Expression)iter.next();
/* 239 */         expression.accept(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void visit(DateValue dateValue)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visit(TimestampValue timestampValue)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visit(TimeValue timeValue)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visit(CaseExpression caseExpression)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visit(WhenClause whenClause)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void visit(AllComparisonExpression allComparisonExpression)
/*     */     {
/* 278 */       allComparisonExpression.GetSubSelect().getSelectBody().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(AnyComparisonExpression anyComparisonExpression) {
/* 282 */       anyComparisonExpression.GetSubSelect().getSelectBody().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(SubJoin subjoin) {
/* 286 */       subjoin.getLeft().accept(this);
/* 287 */       subjoin.getJoin().getRightItem().accept(this);
/*     */     }
/*     */ 
/*     */     public void visit(Concat concat) {
/* 291 */       visitBinaryExpression(concat);
/*     */     }
/*     */ 
/*     */     public void visit(Matches matches) {
/* 295 */       visitBinaryExpression(matches);
/*     */     }
/*     */ 
/*     */     public void visit(BitwiseAnd bitwiseAnd)
/*     */     {
/* 300 */       visitBinaryExpression(bitwiseAnd);
/*     */     }
/*     */ 
/*     */     public void visit(BitwiseOr bitwiseOr)
/*     */     {
/* 305 */       visitBinaryExpression(bitwiseOr);
/*     */     }
/*     */ 
/*     */     public void visit(BitwiseXor bitwiseXor)
/*     */     {
/* 310 */       visitBinaryExpression(bitwiseXor);
/*     */     }
/*     */ 
/*     */     public void visit(StringValue stringValue)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.converter.SelectSqlConverter
 * JD-Core Version:    0.6.2
 */