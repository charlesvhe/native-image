package com.github.charlesvhe.nativeimage;

import com.querydsl.codegen.EntityType;
import com.querydsl.sql.codegen.DefaultNamingStrategy;

public class QueryDslR2dbcNamingStrategy extends DefaultNamingStrategy {
    @Override
    public String getDefaultVariableName(EntityType entityType) {
        // Q类的RelationalPathBase常量以q开头，以免与Entity变量名冲突，方便static import
        String name = super.getDefaultVariableName(entityType);
        name = "q" + name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
}
