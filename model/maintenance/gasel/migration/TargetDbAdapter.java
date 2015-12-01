package gasel.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.cayenne.access.DataNode;
import org.apache.cayenne.access.OperationObserver;
import org.apache.cayenne.access.QueryTranslator;
import org.apache.cayenne.access.trans.QualifierTranslator;
import org.apache.cayenne.access.trans.QueryAssembler;
import org.apache.cayenne.access.types.ExtendedTypeMap;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.dba.PkGenerator;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.query.BatchQuery;
import org.apache.cayenne.query.Query;
import org.apache.cayenne.query.SQLAction;

public class TargetDbAdapter implements DbAdapter {

	private DbAdapter delegate;
	private MapTransformation transformation;

	public TargetDbAdapter(DbAdapter delegate, MapTransformation transformation) {
		this.delegate = delegate;
		this.transformation = transformation;
	}

	public String getBatchTerminator() {
		return delegate.getBatchTerminator();
	}

	@SuppressWarnings("deprecation")
	public DataNode createDataNode(String name) {
		return delegate.createDataNode(name);
	}

	@SuppressWarnings("deprecation")
	public QueryTranslator getQueryTranslator(Query query) throws Exception {
		return delegate.getQueryTranslator(query);
	}

	public QualifierTranslator getQualifierTranslator(
			QueryAssembler queryAssembler) {
		return delegate.getQualifierTranslator(queryAssembler);
	}

	public SQLAction getAction(Query query, DataNode node) {
		return delegate.getAction(query, node);
	}

	public boolean supportsFkConstraints() {
		return delegate.supportsFkConstraints();
	}

	public boolean supportsUniqueConstraints() {
		return delegate.supportsUniqueConstraints();
	}

	public boolean supportsGeneratedKeys() {
		return delegate.supportsGeneratedKeys();
	}

	public boolean supportsBatchUpdates() {
		return delegate.supportsBatchUpdates();
	}

	public String dropTable(DbEntity entity) {
		return delegate.dropTable(transformation.target(entity));
	}

	public String createTable(DbEntity entity) {
		return delegate.createTable(transformation.target(entity));
	}

	@SuppressWarnings("rawtypes")
	public String createUniqueConstraint(DbEntity source, Collection columns) {
		return delegate.createUniqueConstraint(transformation.target(source), columns);
	}

	public String createFkConstraint(DbRelationship rel) {
		return delegate.createFkConstraint(transformation.target(rel));
	}

	public String[] externalTypesForJdbcType(int type) {
		return delegate.externalTypesForJdbcType(type);
	}

	public ExtendedTypeMap getExtendedTypes() {
		return delegate.getExtendedTypes();
	}

	public PkGenerator getPkGenerator() {
		return delegate.getPkGenerator();
	}

	public DbAttribute buildAttribute(String name, String typeName, int type,
			int size, int precision, boolean allowNulls) {
		return delegate.buildAttribute(name, typeName, type, size, precision,
				allowNulls);
	}

	public void bindParameter(PreparedStatement statement, Object object,
			int pos, int sqlType, int precision) throws SQLException, Exception {
		delegate.bindParameter(statement, object, pos, sqlType, precision);
	}

	public String tableTypeForTable() {
		return delegate.tableTypeForTable();
	}

	public String tableTypeForView() {
		return delegate.tableTypeForView();
	}

	public boolean shouldRunBatchQuery(DataNode node, Connection con,
			BatchQuery query, OperationObserver delegate) throws SQLException,
			Exception {
		return true; // obsolete, stub it
	}

}
