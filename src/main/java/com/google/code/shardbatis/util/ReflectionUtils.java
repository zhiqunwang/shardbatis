/*     */ package com.google.code.shardbatis.util;
/*     */ 
/*     */ import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.lang.reflect.*;

/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ 
/*     */ public class ReflectionUtils
/*     */ {
/*  15 */   private static final Log logger = LogFactory.getLog(ReflectionUtils.class);
/*     */ 
/*     */   public static void setFieldValue(Object object, String fieldName, Object value)
/*     */   {
/*  22 */     Field field = getDeclaredField(object, fieldName);
/*     */ 
/*  24 */     if (field == null) {
/*  25 */       throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
/*     */     }
/*  27 */     makeAccessible(field);
/*     */     try
/*     */     {
/*  30 */       field.set(object, value);
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object getFieldValue(Object object, String fieldName)
/*     */   {
/*  40 */     Field field = getDeclaredField(object, fieldName);
/*     */ 
/*  42 */     if (field == null) {
/*  43 */       throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
/*     */     }
/*  45 */     makeAccessible(field);
/*     */ 
/*  47 */     Object result = null;
/*     */     try {
/*  49 */       result = field.get(object);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/*     */     }
/*  53 */     return result;
/*     */   }
/*     */ 
/*     */   public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters)
/*     */     throws InvocationTargetException
/*     */   {
/*  61 */     Method method = getDeclaredMethod(object, methodName, parameterTypes);
/*  62 */     if (method == null) {
/*  63 */       throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
/*     */     }
/*  65 */     method.setAccessible(true);
/*     */     try
/*     */     {
/*  68 */       return method.invoke(object, parameters);
/*     */     }
/*     */     catch (IllegalAccessException e)
/*     */     {
/*     */     }
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   protected static Field getDeclaredField(Object object, String fieldName)
/*     */   {
/*  80 */     for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
/*     */       try
/*     */       {
/*  83 */         return superClass.getDeclaredField(fieldName);
/*     */       }
/*     */       catch (NoSuchFieldException e) {
/*     */       }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   protected static void makeAccessible(Field field)
/*     */   {
/*  94 */     if ((!Modifier.isPublic(field.getModifiers())) || (!Modifier.isPublic(field.getDeclaringClass().getModifiers())))
/*  95 */       field.setAccessible(true);
/*     */   }
/*     */ 
/*     */   protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes)
/*     */   {
/* 103 */     for (Class superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass())
/*     */       try
/*     */       {
/* 106 */         return superClass.getDeclaredMethod(methodName, parameterTypes);
/*     */       }
/*     */       catch (NoSuchMethodException e)
/*     */       {
/*     */       }
/* 111 */     return null;
/*     */   }
/*     */ 
/*     */   public static <T> Class<T> getSuperClassGenricType(Class clazz)
/*     */   {
/* 124 */     return getSuperClassGenricType(clazz, 0);
/*     */   }
/*     */ 
/*     */   public static Class getSuperClassGenricType(Class clazz, int index)
/*     */   {
/* 138 */     Type genType = clazz.getGenericSuperclass();
/*     */ 
/* 140 */     if (!(genType instanceof ParameterizedType)) {
/* 141 */       logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
/* 142 */       return Object.class;
/*     */     }
/*     */ 
/* 145 */     Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
/*     */ 
/* 147 */     if ((index >= params.length) || (index < 0)) {
/* 148 */       logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
/*     */ 
/* 150 */       return Object.class;
/*     */     }
/* 152 */     if (!(params[index] instanceof Class)) {
/* 153 */       logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
/* 154 */       return Object.class;
/*     */     }
/*     */ 
/* 157 */     return (Class)params[index];
/*     */   }
/*     */ 
/*     */   public static IllegalArgumentException convertToUncheckedException(Exception e)
/*     */   {
/* 165 */     if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) || ((e instanceof NoSuchMethodException)))
/*     */     {
/* 167 */       return new IllegalArgumentException("Refelction Exception.", e);
/*     */     }
/* 169 */     return new IllegalArgumentException(e);
/*     */   }
/*     */ }
