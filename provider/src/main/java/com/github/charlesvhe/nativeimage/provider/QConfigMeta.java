package com.github.charlesvhe.nativeimage.provider;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QConfigMeta is a Querydsl query type for ConfigMeta
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QConfigMeta extends com.querydsl.sql.RelationalPathBase<ConfigMeta> {

    private static final long serialVersionUID = -574120039;

    public static final QConfigMeta qConfigMeta = new QConfigMeta("CONFIG_META");

    public final StringPath appId = createString("appId");

    public final StringPath code = createString("code");

    public final StringPath columnName = createString("columnName");

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> gmtCreate = createDateTime("gmtCreate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> gmtModified = createDateTime("gmtModified", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath property = createString("property");

    public final NumberPath<Integer> sort = createNumber("sort", Integer.class);

    public final com.querydsl.sql.PrimaryKey<ConfigMeta> constraint8 = createPrimaryKey(id);

    public QConfigMeta(String variable) {
        super(ConfigMeta.class, forVariable(variable), "PUBLIC", "CONFIG_META");
        addMetadata();
    }

    public QConfigMeta(String variable, String schema, String table) {
        super(ConfigMeta.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QConfigMeta(String variable, String schema) {
        super(ConfigMeta.class, forVariable(variable), schema, "CONFIG_META");
        addMetadata();
    }

    public QConfigMeta(Path<? extends ConfigMeta> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "CONFIG_META");
        addMetadata();
    }

    public QConfigMeta(PathMetadata metadata) {
        super(ConfigMeta.class, metadata, "PUBLIC", "CONFIG_META");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(appId, ColumnMetadata.named("APP_ID").withIndex(2).ofType(Types.VARCHAR).withSize(64));
        addMetadata(code, ColumnMetadata.named("CODE").withIndex(3).ofType(Types.VARCHAR).withSize(64));
        addMetadata(columnName, ColumnMetadata.named("COLUMN_NAME").withIndex(5).ofType(Types.VARCHAR).withSize(64));
        addMetadata(description, ColumnMetadata.named("DESCRIPTION").withIndex(6).ofType(Types.VARCHAR).withSize(128));
        addMetadata(gmtCreate, ColumnMetadata.named("GMT_CREATE").withIndex(8).ofType(Types.TIMESTAMP).withSize(26).withDigits(6));
        addMetadata(gmtModified, ColumnMetadata.named("GMT_MODIFIED").withIndex(9).ofType(Types.TIMESTAMP).withSize(26).withDigits(6));
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(Types.BIGINT).withSize(64).notNull());
        addMetadata(property, ColumnMetadata.named("PROPERTY").withIndex(4).ofType(Types.VARCHAR).withSize(64));
        addMetadata(sort, ColumnMetadata.named("SORT").withIndex(7).ofType(Types.INTEGER).withSize(32));
    }

}

