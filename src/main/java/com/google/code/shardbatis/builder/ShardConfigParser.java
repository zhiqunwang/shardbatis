/*     */ package com.google.code.shardbatis.builder;
/*     */ 
/*     */ import com.google.code.shardbatis.strategy.ShardStrategy;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class ShardConfigParser
/*     */ {
/*  31 */   private static final Log log = LogFactory.getLog(ShardConfigParser.class);
/*     */   private static final String SHARD_CONFIG_DTD = "com/google/code/shardbatis/builder/shardbatis-config.dtd";
/*  34 */   private static final Map<String, String> DOC_TYPE_MAP = new HashMap();
/*     */ 
/*     */   public ShardConfigHolder parse(InputStream input)
/*     */     throws Exception
/*     */   {
/*  50 */     final ShardConfigHolder configHolder = ShardConfigHolder.getInstance();
/*     */ 
/*  53 */     SAXParserFactory spf = SAXParserFactory.newInstance();
/*  54 */     spf.setValidating(true);
/*  55 */     spf.setNamespaceAware(true);
/*  56 */     SAXParser parser = spf.newSAXParser();
/*  57 */     XMLReader reader = parser.getXMLReader();
/*     */ 
/*  59 */     DefaultHandler handler = new DefaultHandler()
/*     */     {
/*     */       private String parentElement;
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
/*  64 */         if ("strategy".equals(qName))
/*     */         {
/*  66 */           String table = attributes.getValue("tableName");
/*     */ 
/*  68 */           String className = attributes.getValue("strategyClass");
/*     */           try {
/*  70 */             Class clazz = Class.forName(className);
/*  71 */             ShardStrategy strategy = (ShardStrategy)clazz.newInstance();
/*     */ 
/*  73 */             configHolder.register(table, strategy);
/*     */           } catch (ClassNotFoundException e) {
/*  75 */             throw new SAXException(e);
/*     */           } catch (InstantiationException e) {
/*  77 */             throw new SAXException(e);
/*     */           } catch (IllegalAccessException e) {
/*  79 */             throw new SAXException(e);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*  84 */         if (("ignoreList".equals(qName)) || ("parseList".equals(qName)))
/*  85 */           this.parentElement = qName;
/*     */       }
/*     */ 
/*     */       public void characters(char[] ch, int start, int length)
/*     */         throws SAXException
/*     */       {
/*  91 */         if ("ignoreList".equals(this.parentElement))
/*  92 */           configHolder.addIgnoreId(new String(ch, start, length).trim());
/*  93 */         else if ("parseList".equals(this.parentElement))
/*  94 */           configHolder.addParseId(new String(ch, start, length).trim());
/*     */       }
/*     */ 
/*     */       public void error(SAXParseException e) throws SAXException
/*     */       {
/*  99 */         throw e;
/*     */       }
/*     */ 
/*     */       public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException
/*     */       {
/* 104 */         if (publicId != null)
/* 105 */           publicId = publicId.toLowerCase();
/* 106 */         if (systemId != null) {
/* 107 */           systemId = systemId.toLowerCase();
/*     */         }
/* 109 */         InputSource source = null;
/*     */         try {
/* 111 */           String path = (String)ShardConfigParser.DOC_TYPE_MAP.get(publicId);
/* 112 */           source = ShardConfigParser.this.getInputSource(path, source);
/* 113 */           if (source == null) {
/* 114 */             path = (String)ShardConfigParser.DOC_TYPE_MAP.get(systemId);
/* 115 */             source = ShardConfigParser.this.getInputSource(path, source);
/*     */           }
/*     */         } catch (Exception e) {
/* 118 */           throw new SAXException(e.toString());
/*     */         }
/* 120 */         return source;
/*     */       }
/*     */     };
/* 124 */     reader.setContentHandler(handler);
/* 125 */     reader.setEntityResolver(handler);
/* 126 */     reader.setErrorHandler(handler);
/* 127 */     reader.parse(new InputSource(input));
/*     */ 
/* 129 */     return configHolder;
/*     */   }
/*     */ 
/*     */   private InputSource getInputSource(String path, InputSource source) {
/* 133 */     if (path != null) {
/* 134 */       InputStream in = null;
/*     */       try {
/* 136 */         in = Resources.getResourceAsStream(path);
/* 137 */         source = new InputSource(in);
/*     */       } catch (IOException e) {
/* 139 */         log.warn(e.getMessage());
/*     */       }
/*     */     }
/* 142 */     return source;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  36 */     DOC_TYPE_MAP.put("http://shardbatis.googlecode.com/dtd/shardbatis-config.dtd".toLowerCase(), "com/google/code/shardbatis/builder/shardbatis-config.dtd");
/*     */ 
/*  39 */     DOC_TYPE_MAP.put("-//shardbatis.googlecode.com//DTD Shardbatis 2.0//EN".toLowerCase(), "com/google/code/shardbatis/builder/shardbatis-config.dtd");
/*     */   }
/*     */ }

/* Location:           E:\wangzhiqun\Desktop\shardbatis-2.0.0B.jar
 * Qualified Name:     com.google.code.shardbatis.builder.ShardConfigParser
 * JD-Core Version:    0.6.2
 */