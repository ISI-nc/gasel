package gasel.migration;

import java.util.Collection;

import org.apache.cayenne.map.DataMap;
import org.apache.cayenne.map.DbAttribute;
import org.apache.cayenne.map.DbEntity;
import org.apache.cayenne.map.DbJoin;
import org.apache.cayenne.map.DbRelationship;
import org.apache.cayenne.map.Relationship;
import org.apache.hivemind.util.Defense;

public abstract class MapTransformation {

	protected abstract String translateName(DbEntity srcEntity);

	private final DataMap map = new DataMap("__target__");
	private Collection<DataMap> sourceMaps;

	public void setup(Collection<DataMap> sourceMaps) {
		if (this.sourceMaps != null) {
			throw new IllegalStateException("Already set up");
		}
		this.sourceMaps = sourceMaps;
		initialize();
	}

	public DataMap getMap() {
		return map;
	}

	public DbEntity target(DbEntity dbEntity) {
		if (dbEntity.getDataMap() == map) {
			return dbEntity; // this is ours
		}
		DbEntity target = map.getDbEntity(translateName(dbEntity));
		Defense.notNull(target, dbEntity.getFullyQualifiedName());
		return target;
	}

	public DbRelationship target(DbRelationship dbRelationship) {
		DbEntity target = target((DbEntity) dbRelationship.getSourceEntity());
		DbRelationship targetRel = (DbRelationship) target
				.getRelationship(dbRelationship.getName());
		Defense.notNull(targetRel, target.getFullyQualifiedName() + "."
				+ dbRelationship.getName());
		return targetRel;
	}

	private void initialize() {
		createAllDbEntities();
		createAllRelationships();
	}

	private void createAllDbEntities() {
		for (Object o : sourceMaps) {
			DataMap map = (DataMap) o;
			for (Object o1 : map.getDbEntities()) {
				DbEntity src = (DbEntity) o1;
				DbEntity dst = new DbEntity(translateName(src));

				for (Object o2 : src.getAttributes()) {
					dst.addAttribute(copyAttribute((DbAttribute) o2));
				}

				this.map.addDbEntity(dst);
			}
		}
	}

	private DbAttribute copyAttribute(DbAttribute src) {
		DbAttribute dst = new DbAttribute();
		dst.setName(src.getName());
		dst.setType(src.getType());
		dst.setGenerated(src.isGenerated());
		dst.setMandatory(src.isMandatory());
		dst.setMaxLength(src.getMaxLength());
		dst.setPrecision(src.getPrecision());
		dst.setPrimaryKey(src.isPrimaryKey());
		return dst;
	}

	private void createAllRelationships() {
		for (Object o : sourceMaps) {
			DataMap map = (DataMap) o;
			for (Object o1 : map.getDbEntities()) {
				DbEntity src = (DbEntity) o1;
				DbEntity dst = target(src);

				for (Object o2 : src.getRelationships()) {
					DbRelationship rel = (DbRelationship) o2;
					if (!sourceMaps
							.contains(rel.getTargetEntity().getDataMap())) {
						continue; // skip dead relationships
					}
					dst.addRelationship(copyRelationship(rel));
				}
			}
		}
	}

	private Relationship copyRelationship(DbRelationship src) {
		DbRelationship dst = new DbRelationship();
		dst.setName(src.getName());
		dst.setSourceEntity(target((DbEntity) src.getSourceEntity()));
		dst.setTargetEntity(target((DbEntity) src.getTargetEntity()));
		dst.setToDependentPK(src.isToDependentPK());
		dst.setToMany(src.isToMany());

//		System.out.println("REL: "
//				+ ((DbEntity) src.getSourceEntity()).getFullyQualifiedName()
//				+ " -> "
//				+ ((DbEntity) src.getTargetEntity()).getFullyQualifiedName());
//		System.out.println("-->: "
//				+ ((DbEntity) dst.getSourceEntity()).getFullyQualifiedName()
//				+ " -> "
//				+ ((DbEntity) dst.getTargetEntity()).getFullyQualifiedName());
		
		for (Object o : src.getJoins()) {
			DbJoin join = (DbJoin) o;
			dst.addJoin(new DbJoin(dst, join.getSourceName(), join
					.getTargetName()));
//			System.out.println("J: " + join.getSourceName() + " -> " + join.getTargetName());
		}
		
//		if (true)
//			throw new RuntimeException("blk");

		return dst;
	}

}
