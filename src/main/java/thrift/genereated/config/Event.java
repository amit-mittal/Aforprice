/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.genereated.config;

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

public class Event implements org.apache.thrift.TBase<Event, Event._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Event");

  private static final org.apache.thrift.protocol.TField EVENT_DATE_FIELD_DESC = new org.apache.thrift.protocol.TField("eventDate", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField DESCRIPTION_FIELD_DESC = new org.apache.thrift.protocol.TField("description", org.apache.thrift.protocol.TType.STRING, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new EventStandardSchemeFactory());
    schemes.put(TupleScheme.class, new EventTupleSchemeFactory());
  }

  public DateObj eventDate; // required
  public String description; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    EVENT_DATE((short)1, "eventDate"),
    DESCRIPTION((short)2, "description");

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
        case 1: // EVENT_DATE
          return EVENT_DATE;
        case 2: // DESCRIPTION
          return DESCRIPTION;
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
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.EVENT_DATE, new org.apache.thrift.meta_data.FieldMetaData("eventDate", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, DateObj.class)));
    tmpMap.put(_Fields.DESCRIPTION, new org.apache.thrift.meta_data.FieldMetaData("description", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Event.class, metaDataMap);
  }

  public Event() {
  }

  public Event(
    DateObj eventDate,
    String description)
  {
    this();
    this.eventDate = eventDate;
    this.description = description;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Event(Event other) {
    if (other.isSetEventDate()) {
      this.eventDate = new DateObj(other.eventDate);
    }
    if (other.isSetDescription()) {
      this.description = other.description;
    }
  }

  public Event deepCopy() {
    return new Event(this);
  }

  @Override
  public void clear() {
    this.eventDate = null;
    this.description = null;
  }

  public DateObj getEventDate() {
    return this.eventDate;
  }

  public Event setEventDate(DateObj eventDate) {
    this.eventDate = eventDate;
    return this;
  }

  public void unsetEventDate() {
    this.eventDate = null;
  }

  /** Returns true if field eventDate is set (has been assigned a value) and false otherwise */
  public boolean isSetEventDate() {
    return this.eventDate != null;
  }

  public void setEventDateIsSet(boolean value) {
    if (!value) {
      this.eventDate = null;
    }
  }

  public String getDescription() {
    return this.description;
  }

  public Event setDescription(String description) {
    this.description = description;
    return this;
  }

  public void unsetDescription() {
    this.description = null;
  }

  /** Returns true if field description is set (has been assigned a value) and false otherwise */
  public boolean isSetDescription() {
    return this.description != null;
  }

  public void setDescriptionIsSet(boolean value) {
    if (!value) {
      this.description = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case EVENT_DATE:
      if (value == null) {
        unsetEventDate();
      } else {
        setEventDate((DateObj)value);
      }
      break;

    case DESCRIPTION:
      if (value == null) {
        unsetDescription();
      } else {
        setDescription((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case EVENT_DATE:
      return getEventDate();

    case DESCRIPTION:
      return getDescription();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case EVENT_DATE:
      return isSetEventDate();
    case DESCRIPTION:
      return isSetDescription();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Event)
      return this.equals((Event)that);
    return false;
  }

  public boolean equals(Event that) {
    if (that == null)
      return false;

    boolean this_present_eventDate = true && this.isSetEventDate();
    boolean that_present_eventDate = true && that.isSetEventDate();
    if (this_present_eventDate || that_present_eventDate) {
      if (!(this_present_eventDate && that_present_eventDate))
        return false;
      if (!this.eventDate.equals(that.eventDate))
        return false;
    }

    boolean this_present_description = true && this.isSetDescription();
    boolean that_present_description = true && that.isSetDescription();
    if (this_present_description || that_present_description) {
      if (!(this_present_description && that_present_description))
        return false;
      if (!this.description.equals(that.description))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Event other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Event typedOther = (Event)other;

    lastComparison = Boolean.valueOf(isSetEventDate()).compareTo(typedOther.isSetEventDate());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEventDate()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.eventDate, typedOther.eventDate);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetDescription()).compareTo(typedOther.isSetDescription());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetDescription()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.description, typedOther.description);
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
    StringBuilder sb = new StringBuilder("Event(");
    boolean first = true;

    sb.append("eventDate:");
    if (this.eventDate == null) {
      sb.append("null");
    } else {
      sb.append(this.eventDate);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("description:");
    if (this.description == null) {
      sb.append("null");
    } else {
      sb.append(this.description);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (eventDate == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'eventDate' was not present! Struct: " + toString());
    }
    if (description == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'description' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (eventDate != null) {
      eventDate.validate();
    }
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
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class EventStandardSchemeFactory implements SchemeFactory {
    public EventStandardScheme getScheme() {
      return new EventStandardScheme();
    }
  }

  private static class EventStandardScheme extends StandardScheme<Event> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Event struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // EVENT_DATE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.eventDate = new DateObj();
              struct.eventDate.read(iprot);
              struct.setEventDateIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // DESCRIPTION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.description = iprot.readString();
              struct.setDescriptionIsSet(true);
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
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Event struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.eventDate != null) {
        oprot.writeFieldBegin(EVENT_DATE_FIELD_DESC);
        struct.eventDate.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.description != null) {
        oprot.writeFieldBegin(DESCRIPTION_FIELD_DESC);
        oprot.writeString(struct.description);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class EventTupleSchemeFactory implements SchemeFactory {
    public EventTupleScheme getScheme() {
      return new EventTupleScheme();
    }
  }

  private static class EventTupleScheme extends TupleScheme<Event> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Event struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      struct.eventDate.write(oprot);
      oprot.writeString(struct.description);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Event struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.eventDate = new DateObj();
      struct.eventDate.read(iprot);
      struct.setEventDateIsSet(true);
      struct.description = iprot.readString();
      struct.setDescriptionIsSet(true);
    }
  }

}

