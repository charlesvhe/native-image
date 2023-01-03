package com.github.charlesvhe.nativeimage.provider;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QConfigItem is a Querydsl query type for ConfigItem
 */
@Generated("com.querydsl.sql.codegen.MetaDataSerializer")
public class QConfigItem extends com.querydsl.sql.RelationalPathBase<ConfigItem> {

    private static final long serialVersionUID = -574225241;

    public static final QConfigItem qConfigItem = new QConfigItem("CONFIG_ITEM");

    public final StringPath appId = createString("appId");

    public final DateTimePath<java.time.LocalDateTime> datetime1 = createDateTime("datetime1", java.time.LocalDateTime.class);

    public final NumberPath<java.math.BigDecimal> decimal1 = createNumber("decimal1", java.math.BigDecimal.class);

    public final DateTimePath<java.time.LocalDateTime> gmtCreate = createDateTime("gmtCreate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> gmtModified = createDateTime("gmtModified", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath metaCode = createString("metaCode");

    public final NumberPath<Long> parentId = createNumber("parentId", Long.class);

    public final NumberPath<Integer> sort = createNumber("sort", Integer.class);

    public final StringPath text1 = createString("text1");

    public final StringPath varchar1 = createString("varchar1");

    public final StringPath varchar2 = createString("varchar2");

    public final StringPath varchar3 = createString("varchar3");

    public final StringPath varchar4 = createString("varchar4");

    public final StringPath varchar5 = createString("varchar5");

    public final com.querydsl.sql.PrimaryKey<ConfigItem> constraint83 = createPrimaryKey(id);

    public QConfigItem(String variable) {
        super(ConfigItem.class, forVariable(variable), "PUBLIC", "CONFIG_ITEM");
        addMetadata();
    }

    public QConfigItem(String variable, String schema, String table) {
        super(ConfigItem.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QConfigItem(String variable, String schema) {
        super(ConfigItem.class, forVariable(variable), schema, "CONFIG_ITEM");
        addMetadata();
    }

    public QConfigItem(Path<? extends ConfigItem> path) {
        super(path.getType(), path.getMetadata(), "PUBLIC", "CONFIG_ITEM");
        addMetadata();
    }

    public QConfigItem(PathMetadata metadata) {
        super(ConfigItem.class, metadata, "PUBLIC", "CONFIG_ITEM");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(appId, ColumnMetadata.named("APP_ID").withIndex(2).ofType(Types.VARCHAR).withSize(64));
        addMetadata(datetime1, ColumnMetadata.named("DATETIME1").withIndex(13).ofType(Types.TIMESTAMP).withSize(26).withDigits(6));
        addMetadata(decimal1, ColumnMetadata.named("DECIMAL1").withIndex(12).ofType(Types.DECIMAL).withSize(19).withDigits(4));
        addMetadata(gmtCreate, ColumnMetadata.named("GMT_CREATE").withIndex(14).ofType(Types.TIMESTAMP).withSize(26).withDigits(6));
        addMetadata(gmtModified, ColumnMetadata.named("GMT_MODIFIED").withIndex(15).ofType(Types.TIMESTAMP).withSize(26).withDigits(6));
        addMetadata(id, ColumnMetadata.named("ID").withIndex(1).ofType(Types.BIGINT).withSize(64).notNull());
        addMetadata(metaCode, ColumnMetadata.named("META_CODE").withIndex(3).ofType(Types.VARCHAR).withSize(64));
        addMetadata(parentId, ColumnMetadata.named("PARENT_ID").withIndex(4).ofType(Types.BIGINT).withSize(64));
        addMetadata(sort, ColumnMetadata.named("SORT").withIndex(5).ofType(Types.INTEGER).withSize(32));
        addMetadata(text1, ColumnMetadata.named("TEXT1").withIndex(11).ofType(Types.CLOB).withSize(2147483647));
        addMetadata(varchar1, ColumnMetadata.named("VARCHAR1").withIndex(6).ofType(Types.VARCHAR).withSize(512));
        addMetadata(varchar2, ColumnMetadata.named("VARCHAR2").withIndex(7).ofType(Types.VARCHAR).withSize(512));
        addMetadata(varchar3, ColumnMetadata.named("VARCHAR3").withIndex(8).ofType(Types.VARCHAR).withSize(512));
        addMetadata(varchar4, ColumnMetadata.named("VARCHAR4").withIndex(9).ofType(Types.VARCHAR).withSize(512));
        addMetadata(varchar5, ColumnMetadata.named("VARCHAR5").withIndex(10).ofType(Types.VARCHAR).withSize(512));
    }

}

