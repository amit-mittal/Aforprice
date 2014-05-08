/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.genereated.retailer;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductList implements org.apache.thrift.TBase<ProductList, ProductList._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ProductList");

  private static final org.apache.thrift.protocol.TField PRODUCTS_FIELD_DESC = new org.apache.thrift.protocol.TField("products", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField TOTAL_COUNT_FIELD_DESC = new org.apache.thrift.protocol.TField("totalCount", org.apache.thrift.protocol.TType.I32, (short)2);
  private static final org.apache.thrift.protocol.TField PRICE_FILTER_TO_NUM_PROD_MAP_FIELD_DESC = new org.apache.thrift.protocol.TField("priceFilterToNumProdMap", org.apache.thrift.protocol.TType.MAP, (short)3);
  private static final org.apache.thrift.protocol.TField REVIEW_FILTER_TO_NUM_PROD_MAP_FIELD_DESC = new org.apache.thrift.protocol.TField("reviewFilterToNumProdMap", org.apache.thrift.protocol.TType.MAP, (short)4);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ProductListStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ProductListTupleSchemeFactory());
  }

  public List<Product> products; // required
  public int totalCount; // required
  public Map<String,Integer> priceFilterToNumProdMap; // optional
  public Map<String,Integer> reviewFilterToNumProdMap; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    PRODUCTS((short)1, "products"),
    TOTAL_COUNT((short)2, "totalCount"),
    PRICE_FILTER_TO_NUM_PROD_MAP((short)3, "priceFilterToNumProdMap"),
    REVIEW_FILTER_TO_NUM_PROD_MAP((short)4, "reviewFilterToNumProdMap");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // PRODUCTS
          return PRODUCTS;
        case 2: // TOTAL_COUNT
          return TOTAL_COUNT;
        case 3: // PRICE_FILTER_TO_NUM_PROD_MAP
          return PRICE_FILTER_TO_NUM_PROD_MAP;
        case 4: // REVIEW_FILTER_TO_NUM_PROD_MAP
          return REVIEW_FILTER_TO_NUM_PROD_MAP;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __TOTALCOUNT_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.PRICE_FILTER_TO_NUM_PROD_MAP,_Fields.REVIEW_FILTER_TO_NUM_PROD_MAP};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.PRODUCTS, new org.apache.thrift.meta_data.FieldMetaData("products", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Product.class))));
    tmpMap.put(_Fields.TOTAL_COUNT, new org.apache.thrift.meta_data.FieldMetaData("totalCount", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
    tmpMap.put(_Fields.PRICE_FILTER_TO_NUM_PROD_MAP, new org.apache.thrift.meta_data.FieldMetaData("priceFilterToNumProdMap", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    tmpMap.put(_Fields.REVIEW_FILTER_TO_NUM_PROD_MAP, new org.apache.thrift.meta_data.FieldMetaData("reviewFilterToNumProdMap", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ProductList.class, metaDataMap);
  }

  public ProductList() {
  }

  public ProductList(
    List<Product> products,
    int totalCount)
  {
    this();
    this.products = products;
    this.totalCount = totalCount;
    setTotalCountIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ProductList(ProductList other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetProducts()) {
      List<Product> __this__products = new ArrayList<Product>();
      for (Product other_element : other.products) {
        __this__products.add(new Product(other_element));
      }
      this.products = __this__products;
    }
    this.totalCount = other.totalCount;
    if (other.isSetPriceFilterToNumProdMap()) {
      Map<String,Integer> __this__priceFilterToNumProdMap = new HashMap<String,Integer>();
      for (Map.Entry<String, Integer> other_element : other.priceFilterToNumProdMap.entrySet()) {

        String other_element_key = other_element.getKey();
        Integer other_element_value = other_element.getValue();

        String __this__priceFilterToNumProdMap_copy_key = other_element_key;

        Integer __this__priceFilterToNumProdMap_copy_value = other_element_value;

        __this__priceFilterToNumProdMap.put(__this__priceFilterToNumProdMap_copy_key, __this__priceFilterToNumProdMap_copy_value);
      }
      this.priceFilterToNumProdMap = __this__priceFilterToNumProdMap;
    }
    if (other.isSetReviewFilterToNumProdMap()) {
      Map<String,Integer> __this__reviewFilterToNumProdMap = new HashMap<String,Integer>();
      for (Map.Entry<String, Integer> other_element : other.reviewFilterToNumProdMap.entrySet()) {

        String other_element_key = other_element.getKey();
        Integer other_element_value = other_element.getValue();

        String __this__reviewFilterToNumProdMap_copy_key = other_element_key;

        Integer __this__reviewFilterToNumProdMap_copy_value = other_element_value;

        __this__reviewFilterToNumProdMap.put(__this__reviewFilterToNumProdMap_copy_key, __this__reviewFilterToNumProdMap_copy_value);
      }
      this.reviewFilterToNumProdMap = __this__reviewFilterToNumProdMap;
    }
  }

  public ProductList deepCopy() {
    return new ProductList(this);
  }

  @Override
  public void clear() {
    this.products = null;
    setTotalCountIsSet(false);
    this.totalCount = 0;
    this.priceFilterToNumProdMap = null;
    this.reviewFilterToNumProdMap = null;
  }

  public int getProductsSize() {
    return (this.products == null) ? 0 : this.products.size();
  }

  public java.util.Iterator<Product> getProductsIterator() {
    return (this.products == null) ? null : this.products.iterator();
  }

  public void addToProducts(Product elem) {
    if (this.products == null) {
      this.products = new ArrayList<Product>();
    }
    this.products.add(elem);
  }

  public List<Product> getProducts() {
    return this.products;
  }

  public ProductList setProducts(List<Product> products) {
    this.products = products;
    return this;
  }

  public void unsetProducts() {
    this.products = null;
  }

  /** Returns true if field products is set (has been assigned a value) and false otherwise */
  public boolean isSetProducts() {
    return this.products != null;
  }

  public void setProductsIsSet(boolean value) {
    if (!value) {
      this.products = null;
    }
  }

  public int getTotalCount() {
    return this.totalCount;
  }

  public ProductList setTotalCount(int totalCount) {
    this.totalCount = totalCount;
    setTotalCountIsSet(true);
    return this;
  }

  public void unsetTotalCount() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __TOTALCOUNT_ISSET_ID);
  }

  /** Returns true if field totalCount is set (has been assigned a value) and false otherwise */
  public boolean isSetTotalCount() {
    return EncodingUtils.testBit(__isset_bitfield, __TOTALCOUNT_ISSET_ID);
  }

  public void setTotalCountIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __TOTALCOUNT_ISSET_ID, value);
  }

  public int getPriceFilterToNumProdMapSize() {
    return (this.priceFilterToNumProdMap == null) ? 0 : this.priceFilterToNumProdMap.size();
  }

  public void putToPriceFilterToNumProdMap(String key, int val) {
    if (this.priceFilterToNumProdMap == null) {
      this.priceFilterToNumProdMap = new HashMap<String,Integer>();
    }
    this.priceFilterToNumProdMap.put(key, val);
  }

  public Map<String,Integer> getPriceFilterToNumProdMap() {
    return this.priceFilterToNumProdMap;
  }

  public ProductList setPriceFilterToNumProdMap(Map<String,Integer> priceFilterToNumProdMap) {
    this.priceFilterToNumProdMap = priceFilterToNumProdMap;
    return this;
  }

  public void unsetPriceFilterToNumProdMap() {
    this.priceFilterToNumProdMap = null;
  }

  /** Returns true if field priceFilterToNumProdMap is set (has been assigned a value) and false otherwise */
  public boolean isSetPriceFilterToNumProdMap() {
    return this.priceFilterToNumProdMap != null;
  }

  public void setPriceFilterToNumProdMapIsSet(boolean value) {
    if (!value) {
      this.priceFilterToNumProdMap = null;
    }
  }

  public int getReviewFilterToNumProdMapSize() {
    return (this.reviewFilterToNumProdMap == null) ? 0 : this.reviewFilterToNumProdMap.size();
  }

  public void putToReviewFilterToNumProdMap(String key, int val) {
    if (this.reviewFilterToNumProdMap == null) {
      this.reviewFilterToNumProdMap = new HashMap<String,Integer>();
    }
    this.reviewFilterToNumProdMap.put(key, val);
  }

  public Map<String,Integer> getReviewFilterToNumProdMap() {
    return this.reviewFilterToNumProdMap;
  }

  public ProductList setReviewFilterToNumProdMap(Map<String,Integer> reviewFilterToNumProdMap) {
    this.reviewFilterToNumProdMap = reviewFilterToNumProdMap;
    return this;
  }

  public void unsetReviewFilterToNumProdMap() {
    this.reviewFilterToNumProdMap = null;
  }

  /** Returns true if field reviewFilterToNumProdMap is set (has been assigned a value) and false otherwise */
  public boolean isSetReviewFilterToNumProdMap() {
    return this.reviewFilterToNumProdMap != null;
  }

  public void setReviewFilterToNumProdMapIsSet(boolean value) {
    if (!value) {
      this.reviewFilterToNumProdMap = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case PRODUCTS:
      if (value == null) {
        unsetProducts();
      } else {
        setProducts((List<Product>)value);
      }
      break;

    case TOTAL_COUNT:
      if (value == null) {
        unsetTotalCount();
      } else {
        setTotalCount((Integer)value);
      }
      break;

    case PRICE_FILTER_TO_NUM_PROD_MAP:
      if (value == null) {
        unsetPriceFilterToNumProdMap();
      } else {
        setPriceFilterToNumProdMap((Map<String,Integer>)value);
      }
      break;

    case REVIEW_FILTER_TO_NUM_PROD_MAP:
      if (value == null) {
        unsetReviewFilterToNumProdMap();
      } else {
        setReviewFilterToNumProdMap((Map<String,Integer>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case PRODUCTS:
      return getProducts();

    case TOTAL_COUNT:
      return Integer.valueOf(getTotalCount());

    case PRICE_FILTER_TO_NUM_PROD_MAP:
      return getPriceFilterToNumProdMap();

    case REVIEW_FILTER_TO_NUM_PROD_MAP:
      return getReviewFilterToNumProdMap();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case PRODUCTS:
      return isSetProducts();
    case TOTAL_COUNT:
      return isSetTotalCount();
    case PRICE_FILTER_TO_NUM_PROD_MAP:
      return isSetPriceFilterToNumProdMap();
    case REVIEW_FILTER_TO_NUM_PROD_MAP:
      return isSetReviewFilterToNumProdMap();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ProductList)
      return this.equals((ProductList)that);
    return false;
  }

  public boolean equals(ProductList that) {
    if (that == null)
      return false;

    boolean this_present_products = true && this.isSetProducts();
    boolean that_present_products = true && that.isSetProducts();
    if (this_present_products || that_present_products) {
      if (!(this_present_products && that_present_products))
        return false;
      if (!this.products.equals(that.products))
        return false;
    }

    boolean this_present_totalCount = true;
    boolean that_present_totalCount = true;
    if (this_present_totalCount || that_present_totalCount) {
      if (!(this_present_totalCount && that_present_totalCount))
        return false;
      if (this.totalCount != that.totalCount)
        return false;
    }

    boolean this_present_priceFilterToNumProdMap = true && this.isSetPriceFilterToNumProdMap();
    boolean that_present_priceFilterToNumProdMap = true && that.isSetPriceFilterToNumProdMap();
    if (this_present_priceFilterToNumProdMap || that_present_priceFilterToNumProdMap) {
      if (!(this_present_priceFilterToNumProdMap && that_present_priceFilterToNumProdMap))
        return false;
      if (!this.priceFilterToNumProdMap.equals(that.priceFilterToNumProdMap))
        return false;
    }

    boolean this_present_reviewFilterToNumProdMap = true && this.isSetReviewFilterToNumProdMap();
    boolean that_present_reviewFilterToNumProdMap = true && that.isSetReviewFilterToNumProdMap();
    if (this_present_reviewFilterToNumProdMap || that_present_reviewFilterToNumProdMap) {
      if (!(this_present_reviewFilterToNumProdMap && that_present_reviewFilterToNumProdMap))
        return false;
      if (!this.reviewFilterToNumProdMap.equals(that.reviewFilterToNumProdMap))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(ProductList other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    ProductList typedOther = (ProductList)other;

    lastComparison = Boolean.valueOf(isSetProducts()).compareTo(typedOther.isSetProducts());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetProducts()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.products, typedOther.products);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTotalCount()).compareTo(typedOther.isSetTotalCount());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTotalCount()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.totalCount, typedOther.totalCount);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPriceFilterToNumProdMap()).compareTo(typedOther.isSetPriceFilterToNumProdMap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPriceFilterToNumProdMap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.priceFilterToNumProdMap, typedOther.priceFilterToNumProdMap);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetReviewFilterToNumProdMap()).compareTo(typedOther.isSetReviewFilterToNumProdMap());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReviewFilterToNumProdMap()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.reviewFilterToNumProdMap, typedOther.reviewFilterToNumProdMap);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ProductList(");
    boolean first = true;

    sb.append("products:");
    if (this.products == null) {
      sb.append("null");
    } else {
      sb.append(this.products);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("totalCount:");
    sb.append(this.totalCount);
    first = false;
    if (isSetPriceFilterToNumProdMap()) {
      if (!first) sb.append(", ");
      sb.append("priceFilterToNumProdMap:");
      if (this.priceFilterToNumProdMap == null) {
        sb.append("null");
      } else {
        sb.append(this.priceFilterToNumProdMap);
      }
      first = false;
    }
    if (isSetReviewFilterToNumProdMap()) {
      if (!first) sb.append(", ");
      sb.append("reviewFilterToNumProdMap:");
      if (this.reviewFilterToNumProdMap == null) {
        sb.append("null");
      } else {
        sb.append(this.reviewFilterToNumProdMap);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (products == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'products' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'totalCount' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ProductListStandardSchemeFactory implements SchemeFactory {
    public ProductListStandardScheme getScheme() {
      return new ProductListStandardScheme();
    }
  }

  private static class ProductListStandardScheme extends StandardScheme<ProductList> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ProductList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // PRODUCTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list40 = iprot.readListBegin();
                struct.products = new ArrayList<Product>(_list40.size);
                for (int _i41 = 0; _i41 < _list40.size; ++_i41)
                {
                  Product _elem42; // required
                  _elem42 = new Product();
                  _elem42.read(iprot);
                  struct.products.add(_elem42);
                }
                iprot.readListEnd();
              }
              struct.setProductsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // TOTAL_COUNT
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.totalCount = iprot.readI32();
              struct.setTotalCountIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PRICE_FILTER_TO_NUM_PROD_MAP
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map43 = iprot.readMapBegin();
                struct.priceFilterToNumProdMap = new HashMap<String,Integer>(2*_map43.size);
                for (int _i44 = 0; _i44 < _map43.size; ++_i44)
                {
                  String _key45; // required
                  int _val46; // required
                  _key45 = iprot.readString();
                  _val46 = iprot.readI32();
                  struct.priceFilterToNumProdMap.put(_key45, _val46);
                }
                iprot.readMapEnd();
              }
              struct.setPriceFilterToNumProdMapIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // REVIEW_FILTER_TO_NUM_PROD_MAP
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map47 = iprot.readMapBegin();
                struct.reviewFilterToNumProdMap = new HashMap<String,Integer>(2*_map47.size);
                for (int _i48 = 0; _i48 < _map47.size; ++_i48)
                {
                  String _key49; // required
                  int _val50; // required
                  _key49 = iprot.readString();
                  _val50 = iprot.readI32();
                  struct.reviewFilterToNumProdMap.put(_key49, _val50);
                }
                iprot.readMapEnd();
              }
              struct.setReviewFilterToNumProdMapIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetTotalCount()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'totalCount' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ProductList struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.products != null) {
        oprot.writeFieldBegin(PRODUCTS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.products.size()));
          for (Product _iter51 : struct.products)
          {
            _iter51.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(TOTAL_COUNT_FIELD_DESC);
      oprot.writeI32(struct.totalCount);
      oprot.writeFieldEnd();
      if (struct.priceFilterToNumProdMap != null) {
        if (struct.isSetPriceFilterToNumProdMap()) {
          oprot.writeFieldBegin(PRICE_FILTER_TO_NUM_PROD_MAP_FIELD_DESC);
          {
            oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I32, struct.priceFilterToNumProdMap.size()));
            for (Map.Entry<String, Integer> _iter52 : struct.priceFilterToNumProdMap.entrySet())
            {
              oprot.writeString(_iter52.getKey());
              oprot.writeI32(_iter52.getValue());
            }
            oprot.writeMapEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.reviewFilterToNumProdMap != null) {
        if (struct.isSetReviewFilterToNumProdMap()) {
          oprot.writeFieldBegin(REVIEW_FILTER_TO_NUM_PROD_MAP_FIELD_DESC);
          {
            oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I32, struct.reviewFilterToNumProdMap.size()));
            for (Map.Entry<String, Integer> _iter53 : struct.reviewFilterToNumProdMap.entrySet())
            {
              oprot.writeString(_iter53.getKey());
              oprot.writeI32(_iter53.getValue());
            }
            oprot.writeMapEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ProductListTupleSchemeFactory implements SchemeFactory {
    public ProductListTupleScheme getScheme() {
      return new ProductListTupleScheme();
    }
  }

  private static class ProductListTupleScheme extends TupleScheme<ProductList> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ProductList struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      {
        oprot.writeI32(struct.products.size());
        for (Product _iter54 : struct.products)
        {
          _iter54.write(oprot);
        }
      }
      oprot.writeI32(struct.totalCount);
      BitSet optionals = new BitSet();
      if (struct.isSetPriceFilterToNumProdMap()) {
        optionals.set(0);
      }
      if (struct.isSetReviewFilterToNumProdMap()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetPriceFilterToNumProdMap()) {
        {
          oprot.writeI32(struct.priceFilterToNumProdMap.size());
          for (Map.Entry<String, Integer> _iter55 : struct.priceFilterToNumProdMap.entrySet())
          {
            oprot.writeString(_iter55.getKey());
            oprot.writeI32(_iter55.getValue());
          }
        }
      }
      if (struct.isSetReviewFilterToNumProdMap()) {
        {
          oprot.writeI32(struct.reviewFilterToNumProdMap.size());
          for (Map.Entry<String, Integer> _iter56 : struct.reviewFilterToNumProdMap.entrySet())
          {
            oprot.writeString(_iter56.getKey());
            oprot.writeI32(_iter56.getValue());
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ProductList struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      {
        org.apache.thrift.protocol.TList _list57 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
        struct.products = new ArrayList<Product>(_list57.size);
        for (int _i58 = 0; _i58 < _list57.size; ++_i58)
        {
          Product _elem59; // required
          _elem59 = new Product();
          _elem59.read(iprot);
          struct.products.add(_elem59);
        }
      }
      struct.setProductsIsSet(true);
      struct.totalCount = iprot.readI32();
      struct.setTotalCountIsSet(true);
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TMap _map60 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I32, iprot.readI32());
          struct.priceFilterToNumProdMap = new HashMap<String,Integer>(2*_map60.size);
          for (int _i61 = 0; _i61 < _map60.size; ++_i61)
          {
            String _key62; // required
            int _val63; // required
            _key62 = iprot.readString();
            _val63 = iprot.readI32();
            struct.priceFilterToNumProdMap.put(_key62, _val63);
          }
        }
        struct.setPriceFilterToNumProdMapIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TMap _map64 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.I32, iprot.readI32());
          struct.reviewFilterToNumProdMap = new HashMap<String,Integer>(2*_map64.size);
          for (int _i65 = 0; _i65 < _map64.size; ++_i65)
          {
            String _key66; // required
            int _val67; // required
            _key66 = iprot.readString();
            _val67 = iprot.readI32();
            struct.reviewFilterToNumProdMap.put(_key66, _val67);
          }
        }
        struct.setReviewFilterToNumProdMapIsSet(true);
      }
    }
  }

}
