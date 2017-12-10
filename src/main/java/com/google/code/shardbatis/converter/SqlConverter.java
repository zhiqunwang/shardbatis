package com.google.code.shardbatis.converter;

import net.sf.jsqlparser.statement.Statement;

public abstract interface SqlConverter
{
  public abstract String convert(Statement paramStatement, Object paramObject, String paramString);
}
