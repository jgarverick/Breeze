/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JerseyBreeze.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.IteratorUtils;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.Mapping;
import org.hibernate.id.*;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Selectable;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.AssociationType;
import org.hibernate.type.ComponentType;
import org.hibernate.type.OneToOneType;
import org.hibernate.type.Type;

/**
 *
 * @author Josh
 */
    /// <summary>
    /// Builds a data structure containing the metadata required by Breeze.
    /// <see cref="http://www.breezejs.com/documentation/breeze-metadata-format"/>
    /// </summary>

        /// <summary>
        /// Get the column names for a given property as a comma-delimited String of unbracketed names.
        /// </summary>
        /// <param name="persister"></param>
        /// <param name="propertyName"></param>
        /// <returns></returns>
    public class HBreezeMetadata
    {
        private SessionFactory _sessionFactory;
        private Configuration _configuration;
        private Map<String, Object> _map;
        private List<Map<String, Object>> _typeList;
        private Map<String, Object> _resourceMap;
        private HashSet<String> _typeNames;
        private Map<String, String> _fkMap;
        private final String ONE2ONE = "_1to1";
        private final String ASSN = "AN_";
        public static String FK_MAP = "fkMap";

        public HBreezeMetadata(SessionFactory sessionFactory, Configuration configuration)
        {
            _sessionFactory = sessionFactory;
            _configuration = configuration;
        }

        /// <summary>
        /// Build the Breeze metadata as a nested Dictionary.  
        /// The result can be converted to JSON and sent to the Breeze client.
        /// </summary>
        /// <returns></returns>
        public Map<String, Object> BuildMetadata() throws Exception
        {
            InitMap();

            Map classMeta;
            //IDictionary<String, ICollectionMetadata> collectionMeta = _sessionFactory.GetAllCollectionMetadata();
            classMeta = _sessionFactory.getAllClassMetadata();

            for (int i=0;i<  classMeta.values().size();i++)
            {
                ClassMetadata meta = (ClassMetadata)classMeta.values().toArray()[i];
                AddClass(meta);
            }
            return _map;
        }

        /// <summary>
        /// Populate the metadata header.
        /// </summary>
        void InitMap()
        {
            _map = new HashMap<String, Object>();
            _typeList = new ArrayList<Map<String, Object>>();
            _typeNames = new HashSet<String>();
            _resourceMap = new HashMap<String, Object>();
            _fkMap = new HashMap<String, String>();
            _map.put("metadataVersion", "1.0.4");
            _map.put("localQueryComparisonOptions", "caseInsensitiveSQL");
            _map.put("structuralTypes", _typeList);
            _map.put("resourceEntityTypeMap",_resourceMap);
            _map.put(FK_MAP, _fkMap);
        }

        /// <summary>
        /// Add the metadata for an entity.
        /// </summary>
        /// <param name="meta"></param>
        void AddClass(ClassMetadata meta) throws Exception
        {
           Class<?> type = meta.getMappedClass(EntityMode.POJO);

            // "Customer:#Breeze.Nhibernate.NorthwindIBModel": {
            String classKey = type.getName() + ":#" + type.getPackage().getName();
            Map cmap = new HashMap();
            _typeList.add(cmap);

            cmap.put("shortName", type.getSimpleName());
            cmap.put("namespace", type.getPackage().getName());

            EntityPersister entityPersister = (EntityPersister)meta;
            IdentifierGenerator generator = entityPersister != null ? entityPersister.getIdentifierGenerator() : null;
            if (generator != null)
            {
                String genType = null;
                if (generator.getClass() == IdentityGenerator.class) {
                    genType = "Identity";
                } else if (generator.getClass() == org.hibernate.id.Assigned.class) {
                genType = "None";
            }
                else {
                genType = "KeyGenerator";
            }
                cmap.put("autoGeneratedKeyType", genType); // TODO find the real generator
            }

            String resourceName = Pluralize(type.getName()); // TODO find the real name
            cmap.put("defaultResourceName", resourceName);
            _resourceMap.put(resourceName, classKey);

            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            cmap.put("dataProperties", dataList);
            List<Map<String, Object>> navList = new ArrayList<Map<String, Object>>();
            cmap.put("navigationProperties", navList);

            PersistentClass persistentClass = _configuration.getClassMapping(type.getName());
            AddClassProperties(meta, persistentClass, dataList, navList);
        }

        /// <summary>
        /// Add the properties for an entity.
        /// </summary>
        /// <param name="meta"></param>
        /// <param name="pClass"></param>
        /// <param name="dataList">will be populated with the data properties of the entity</param>
        /// <param name="navList">will be populated with the navigation properties of the entity</param>
        void AddClassProperties(ClassMetadata meta, PersistentClass pClass, List<Map<String, Object>> dataList, List<Map<String, Object>> navList) throws Exception
        {
            // maps column names to their related data properties.  Used in MakeAssociationProperty to convert FK column names to entity property names.
            HashMap<String, Map<String, Object>> relatedDataPropertyMap = new HashMap<String, Map<String, Object>>();

            AbstractEntityPersister persister = (AbstractEntityPersister)meta ;
            Class<?> type = meta.getMappedClass(EntityMode.POJO);

            String[] propNames = meta.getPropertyNames();
            Type[] propTypes = meta.getPropertyTypes();
            boolean[] propNull = meta.getPropertyNullability();
            for (int i = 0; i < propNames.length; i++)
            {
                Type propType = propTypes[i];
                if (!propType.isAssociationType())    // skip association types until we handle all the data types, so the relatedDataPropertyMap will be populated.
                {
                    String propName = propNames[i];
                    List<Selectable> propColumns = IteratorUtils.toList(pClass.getProperty(propName).getColumnIterator());
                    if (propType.isComponentType())
                    {
                        // complex type
                        ComponentType compType = (ComponentType)propType;
                        
                        String complexTypeName = AddComponent(compType, propColumns);
                        Map<String, Object> compMap = new HashMap<String, Object>();
                        compMap.put("nameOnServer", propName);
                        compMap.put("complexTypeName", complexTypeName);
                        compMap.put("isNullable", propNull[i]);
                        dataList.add(compMap);
                    }
                    else
                    {
                        // data property
                        Column col = propColumns.size() == 1 ? (Column)propColumns.get(0) : null;
                        boolean isKey = meta.hasNaturalIdentifier()&& Arrays.asList(meta.getNaturalIdentifierProperties()).contains(i) ;
                        boolean isVersion = meta.isVersioned() && i == meta.getVersionProperty();

                        Map<String,Object> dmap = MakeDataProperty(propName, propType.getName(), propNull[i], col, isKey, isVersion);
                        dataList.add(dmap);

                        String columnNameString = GetPropertyColumnNames(persister, propName); 
                        relatedDataPropertyMap.put(columnNameString, dmap);
                    }
                }
            }


            // Hibernate identifiers are excluded from the list of data properties, so we have to add them separately
            if (meta.hasIdentifierProperty())
            {
                Map<String,Object> dmap = MakeDataProperty(meta.getIdentifierPropertyName(), meta.getIdentifierType().getName(), false, null, true, false);
                dataList.add(0, dmap);

                String columnNameString = GetPropertyColumnNames(persister, meta.getIdentifierPropertyName());
                relatedDataPropertyMap.put(columnNameString, dmap);
            }
            else if (meta.getIdentifierType() != null && meta.getIdentifierType().isComponentType())
            {
                // composite key is a ComponentType
                ComponentType compType = (ComponentType)meta.getIdentifierType();
                String[] compNames = compType.getPropertyNames();
                for (int i = 0; i < compNames.length; i++)
                {
                    Type propType = compType.getSubtypes()[i];
                    if (!propType.isAssociationType())
                    {
                        Map<String,Object> dmap = MakeDataProperty(compNames[i], propType.getName(), compType.getPropertyNullability()[i], null, true, false);
                        dataList.add(0, dmap);
                    }
                    else
                    {
                        // ManyToOneType manyToOne = (ManyToOneType)propType;
                        //Object joinable = manyToOne.GetAssociatedJoinable(this._sessionFactory);
                        String propColumnNames = GetPropertyColumnNames(persister, compNames[i]);

                        Map<String,Object> assProp = MakeAssociationProperty(type, (AssociationType)propType, compNames[i], propColumnNames, pClass, relatedDataPropertyMap, true);
                        navList.add(assProp);
                    }
                }
            }

            // We do the association properties after the data properties, so we can do the foreign key lookups
            for (int i = 0; i < propNames.length; i++)
            {
                //Object propColumnNames = persister.GetPropertyColumnNames(i);
                Type propType = propTypes[i];
                if (propType.isAssociationType())
                {
                    // navigation property
                    String propName = propNames[i];
                    //if (propColumnNames.Length == 0)
                    //    propColumnNames = persister.KeyColumnNames;

                    String propColumnNames = GetPropertyColumnNames(persister, propName);
                    Map<String,Object> assProp = MakeAssociationProperty(type, (AssociationType)propType, propName, propColumnNames, pClass, relatedDataPropertyMap, false);
                    navList.add(assProp);
                }
            }
        }

        /// <summary>
        /// Adds a complex type definition
        /// </summary>
        /// <param name="compType">The complex type</param>
        /// <param name="propColumns">The columns which the complex type spans.  These are used to get the length and defaultValues</param>
        /// <returns>The class name and namespace in the form "Location:#Breeze.Nhibernate.NorthwindIBModel"</returns>
        String AddComponent(ComponentType compType, List<Selectable> propColumns)
        {
            Class<?> type = compType.getReturnedClass();

            // "Location:#Breeze.Nhibernate.NorthwindIBModel"
            String classKey = type.getName() + ":#" + type.getPackage().getName();
            if (_typeNames.contains(classKey))
            {
                // Only add a complex type definition once.
                return classKey;
            }

            Map<String, Object> cmap = new HashMap<String, Object>();
            _typeList.add(0, cmap);
            _typeNames.add(classKey);

            cmap.put("shortName", type.getSimpleName());
            cmap.put("namespace", type.getPackage().getName());
            cmap.put("isComplexType", true);

            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
            cmap.put("dataProperties", dataList);

            String[] propNames = compType.getPropertyNames();
            Type[] propTypes = compType.getSubtypes();
            boolean[] propNull = compType.getPropertyNullability();

            Integer colIndex = 0;
            for (int i = 0; i < propNames.length; i++)
            {
                Type propType = propTypes[i];
                String propName = propNames[i];
                if (propType.isComponentType())
                {
                    // nested complex type
                    ComponentType compType2 = (ComponentType)propType;
                    Integer span = compType2.getColumnSpan((Mapping) _sessionFactory);
                    List<Selectable> subColumns = propColumns.subList(colIndex, span);
                    String complexTypeName = AddComponent(compType2, subColumns);
                    Map<String,Object> compMap = new HashMap<String, Object>();
                    compMap.put("nameOnServer", propName);
                    compMap.put("complexTypeName", complexTypeName);
                    compMap.put("isNullable", propNull[i]);
                    dataList.add(compMap);
                    colIndex += span;
                }
                else
                {
                    // data property
                    Column col = (Column)propColumns.get(colIndex);
                    Map<String,Object> dmap = MakeDataProperty(propName, propType.getName(), propNull[i], col, false, false);
                    dataList.add(dmap);
                    colIndex++;
                }
            }
            return classKey;
        }

        /// <summary>
        /// Make data property metadata for the entity
        /// </summary>
        /// <param name="propName">name of the property on the server</param>
        /// <param name="typeName">data type of the property, e.g. Int32</param>
        /// <param name="isNullable">whether the property is nullable in the database</param>
        /// <param name="col">Column Object, used for maxLength and defaultValue</param>
        /// <param name="isKey">true if this property is part of the key for the entity</param>
        /// <param name="isVersion">true if this property contains the version of the entity (for a concurrency strategy)</param>
        /// <returns></returns>
        private Map<String, Object> MakeDataProperty(String propName, String typeName, boolean isNullable, Column col, boolean isKey, boolean isVersion)
        {
            if (typeName == "byte[]" || typeName == "BinaryBlob") typeName = "Binary";  // breeze expects Binary
            if (typeName == "TimeAsTimeSpan") typeName = "Time";

            Map<String, Object> dmap = new HashMap<String, Object>();
            dmap.put("nameOnServer", propName);
            dmap.put("dataType", typeName);
            dmap.put("isNullable", isNullable);

            if (col != null && col.getDefaultValue() != null)
            {
                dmap.put("defaultValue", col.getDefaultValue());
            }
            if (isKey)
            {
                dmap.put("isPartOfKey", true);
            }
            if (isVersion)
            {
                dmap.put("concurrencyMode", "Fixed");
            }

            List<Map<String, String>> validators = new ArrayList<Map<String,String>>();

            if (!isNullable)
            {
                Map<String, String> valids = new HashMap<String,String>();
                valids.put("name","required");
                validators.add(valids);
            }
            if (col != null && col.getLength()>0)
            {
                dmap.put("maxLength", col.getLength());
                final Integer len = col.getLength();
                validators.add(new HashMap<String, String>() {{
                    put("maxLength", len.toString());
                    put("name", "maxLength");
                }});
            }

            final String validationType=ValidationTypeMap.get(typeName);
            if (validationType != null)
            {
                validators.add(new HashMap<String, String>() {{
                    put("name", validationType);
                }});
            }

            if (validators.size()>0)
                dmap.put("validators", validators);

            return dmap;
        }


        /// <summary>
        /// Make association property metadata for the entity.
        /// Also populates the _fkMap which is used for related-entity fixup in NHContext.FixupRelationships
        /// </summary>
        /// <param name="propType"></param>
        /// <param name="propName"></param>
        /// <param name="pClass"></param>
        /// <param name="relatedDataPropertyMap"></param>
        /// <returns></returns>
        private Map<String, Object> MakeAssociationProperty(Class<?> containingType, AssociationType propType, String propName, String columnNames, PersistentClass pClass, Map<String, Map<String, Object>> relatedDataPropertyMap, boolean isKey) throws Exception
        {
            Map<String, Object> nmap = new HashMap<String, Object>();
            nmap.put("nameOnServer", propName);

            Class<?> relatedEntityType = GetEntityType(propType.getReturnedClass(), propType.isCollectionType());
            nmap.put("entityTypeName", relatedEntityType.getSimpleName() + ":#" + relatedEntityType.getPackage().getName());
            nmap.put("isScalar", !propType.isCollectionType());

            // the associationName must be the same at both ends of the association.
            nmap.put("associationName", GetAssociationName(containingType.getName(), relatedEntityType.getName(), propType.getClass() == OneToOneType.class));

            // The foreign key columns usually applies for many-to-one and one-to-one associations
            if (!propType.isCollectionType())
            {
                String entityRelationship = pClass.getEntityName() + '.' + propName;
                Map<String, Object> relatedDataProperty = relatedDataPropertyMap.get(columnNames);
                if (relatedDataProperty != null)
                {
                    String fkName = (String) relatedDataProperty.get("nameOnServer");
                    nmap.put("foreignKeyNamesOnServer", new String[] { fkName });
                    _fkMap.put(entityRelationship, fkName);
                    if (isKey)
                    {
                        if (relatedDataProperty.get("isPartOfKey") == null)
                        {
                            relatedDataProperty.put("isPartOfKey", true);
                        }
                    }
                }
                else
                {
                    nmap.put("foreignKeyNamesOnServer", columnNames);
                    nmap.put("ERROR", "Could not find matching fk for property " + entityRelationship);
                    _fkMap.put(entityRelationship, columnNames);
                    throw new Exception("Could not find matching fk for property " + entityRelationship);
                }
            }
            return nmap;
        }
        String GetPropertyColumnNames(AbstractEntityPersister persister, String propertyName)
        {
            String[] propColumnNames = persister.getPropertyColumnNames(propertyName);
            if (propColumnNames.length == 0)
            {
                // this happens when the property is part of the key
                propColumnNames = persister.getKeyColumnNames();
            }
            StringBuilder sb = new StringBuilder();
            for (String s : propColumnNames)
            {
                if (sb.length() > 0) sb.append(',');
                sb.append(UnBracket(s));
            }
            return sb.toString();
        }

        /// <summary>
        /// Get the column name without square brackets around it.  E.g. "[OrderID]" -> "OrderID" 
        /// Because sometimes Hibernate gives us brackets, and sometimes it doesn't.
        /// </summary>
        /// <param name="name"></param>
        /// <returns></returns>
        String UnBracket(String name)
        {
            return (name.charAt(0) == '[') ? name.substring(1, name.length() - 2) : name;
        }

        /// <summary>
        /// Get the Breeze name of the entity type.
        /// For collections, Breeze expects the name of the element type.
        /// </summary>
        /// <param name="type"></param>
        /// <param name="isCollectionType"></param>
        /// <returns></returns>
        Class<?> GetEntityType(Class<?> type, boolean isCollectionType) throws Exception
        {
            if (!isCollectionType)
            {
                return type;
            }
            else if (type.isPrimitive())
            {
                return type.getDeclaringClass();
            }
            else if (type.isAnonymousClass())
            {
                return type.getSuperclass();
            }
            throw new Exception("Don't know how to handle " + type);
        }

        /// <summary>
        /// lame pluralizer.  Assumes we just need to add a suffix.
        /// </summary>
        /// <param name="s"></param>
        /// <returns></returns>
        String Pluralize(String s)
        {
            if (s == null || s.length() ==0) return s;
            Integer last = s.length() - 1;
            char c = s.charAt(last);
            switch (c)
            {
                case 'y':
                    return s.substring(0, last) + "ies";
                default:
                    return s + 's';
            }
        }

        /// <summary>
        /// Creates an association name from two entity names.
        /// For consistency, puts the entity names in alphabetical order.
        /// </summary>
        /// <param name="name1"></param>
        /// <param name="name2"></param>
        /// <param name="isOneToOne">if true, adds the one-to-one suffix</param>
        /// <returns></returns>
        String GetAssociationName(String name1, String name2, boolean isOneToOne)
        {
            if (name1.compareTo(name2) < 0)
                return ASSN + name1 + '_' + name2 + (isOneToOne ? ONE2ONE : "");
            else
                return ASSN + name2 + '_' + name1 + (isOneToOne ? ONE2ONE : "");
        }


        // Map of data type to Breeze validation type
        static Dictionary<String, String> ValidationTypeMap = new Hashtable<String, String>() {{
                    put("boolean", "bool" );
                    put("byte", "byte" );
                    put("Date", "date");
                    put("decimal", "number" );
                    put("Guid", "guid" );
                    put("integer", "integer" );
                    put("int", "integer" );
                    put("big_integer", "integer");
                    put("big_decimal", "number" );
                    put("Time", "duration" );
                    put("string","string");
                }};
        

    }


