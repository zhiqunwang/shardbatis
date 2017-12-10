/*     */ package com.google.code.shardbatis.builder;
/*     */ 
/*     */ import com.google.code.shardbatis.strategy.ShardStrategy;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ShardConfigHolder
/*     */ {
/*  18 */   private static final ShardConfigHolder instance = new ShardConfigHolder();
/*     */ 
/*  24 */   private Map<String, ShardStrategy> strategyRegister = new HashMap();
/*     */   private Set<String> ignoreSet;
/*     */   private Set<String> parseSet;
/*     */ 
/*     */   public static ShardConfigHolder getInstance()
/*     */   {
/*  21 */     return instance;
/*     */   }
/*     */ 
/*     */   public void register(String table, ShardStrategy strategy)
/*     */   {
/*  39 */     this.strategyRegister.put(table.toLowerCase(), strategy);
/*     */   }
/*     */ 
/*     */   public ShardStrategy getStrategy(String table)
/*     */   {
/*  49 */     return (ShardStrategy)this.strategyRegister.get(table.toLowerCase());
/*     */   }
/*     */ 
/*     */   public synchronized void addIgnoreId(String id)
/*     */   {
/*  58 */     if (this.ignoreSet == null) {
/*  59 */       this.ignoreSet = new HashSet();
/*     */     }
/*  61 */     this.ignoreSet.add(id);
/*     */   }
/*     */ 
/*     */   public synchronized void addParseId(String id)
/*     */   {
/*  70 */     if (this.parseSet == null) {
/*  71 */       this.parseSet = new HashSet();
/*     */     }
/*  73 */     this.parseSet.add(id);
/*     */   }
/*     */ 
/*     */   public boolean isConfigParseId()
/*     */   {
/*  83 */     return this.parseSet != null;
/*     */   }
/*     */ 
/*     */   public boolean isParseId(String id)
/*     */   {
/*  93 */     return (this.parseSet != null) && (this.parseSet.contains(id));
/*     */   }
/*     */ 
/*     */   public boolean isIgnoreId(String id)
/*     */   {
/* 103 */     return (this.ignoreSet != null) && (this.ignoreSet.contains(id));
/*     */   }
/*     */ }
