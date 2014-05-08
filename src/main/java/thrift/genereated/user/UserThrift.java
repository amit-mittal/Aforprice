/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.genereated.user;

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

public class UserThrift implements org.apache.thrift.TBase<UserThrift, UserThrift._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("UserThrift");

  private static final org.apache.thrift.protocol.TField EMAIL_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("emailId", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField PASSWORD_FIELD_DESC = new org.apache.thrift.protocol.TField("password", org.apache.thrift.protocol.TType.STRING, (short)3);
  private static final org.apache.thrift.protocol.TField COUNTRY_FIELD_DESC = new org.apache.thrift.protocol.TField("country", org.apache.thrift.protocol.TType.STRING, (short)4);
  private static final org.apache.thrift.protocol.TField PHONE_FIELD_DESC = new org.apache.thrift.protocol.TField("phone", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField LAST_LOGGED_IN_FIELD_DESC = new org.apache.thrift.protocol.TField("lastLoggedIn", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField ACTIVE_FIELD_DESC = new org.apache.thrift.protocol.TField("active", org.apache.thrift.protocol.TType.BOOL, (short)7);
  private static final org.apache.thrift.protocol.TField NEWSLETTER_FIELD_DESC = new org.apache.thrift.protocol.TField("newsletter", org.apache.thrift.protocol.TType.BOOL, (short)8);
  private static final org.apache.thrift.protocol.TField REGISTERED_FIELD_DESC = new org.apache.thrift.protocol.TField("registered", org.apache.thrift.protocol.TType.BOOL, (short)9);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new UserThriftStandardSchemeFactory());
    schemes.put(TupleScheme.class, new UserThriftTupleSchemeFactory());
  }

  public String emailId; // required
  public String name; // optional
  public String password; // optional
  public String country; // optional
  public String phone; // optional
  public String lastLoggedIn; // optional
  public boolean active; // optional
  public boolean newsletter; // optional
  public boolean registered; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    EMAIL_ID((short)1, "emailId"),
    NAME((short)2, "name"),
    PASSWORD((short)3, "password"),
    COUNTRY((short)4, "country"),
    PHONE((short)5, "phone"),
    LAST_LOGGED_IN((short)6, "lastLoggedIn"),
    ACTIVE((short)7, "active"),
    NEWSLETTER((short)8, "newsletter"),
    REGISTERED((short)9, "registered");

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
        case 1: // EMAIL_ID
          return EMAIL_ID;
        case 2: // NAME
          return NAME;
        case 3: // PASSWORD
          return PASSWORD;
        case 4: // COUNTRY
          return COUNTRY;
        case 5: // PHONE
          return PHONE;
        case 6: // LAST_LOGGED_IN
          return LAST_LOGGED_IN;
        case 7: // ACTIVE
          return ACTIVE;
        case 8: // NEWSLETTER
          return NEWSLETTER;
        case 9: // REGISTERED
          return REGISTERED;
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
  private static final int __ACTIVE_ISSET_ID = 0;
  private static final int __NEWSLETTER_ISSET_ID = 1;
  private static final int __REGISTERED_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.NAME,_Fields.PASSWORD,_Fields.COUNTRY,_Fields.PHONE,_Fields.LAST_LOGGED_IN,_Fields.ACTIVE,_Fields.NEWSLETTER,_Fields.REGISTERED};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.EMAIL_ID, new org.apache.thrift.meta_data.FieldMetaData("emailId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PASSWORD, new org.apache.thrift.meta_data.FieldMetaData("password", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.COUNTRY, new org.apache.thrift.meta_data.FieldMetaData("country", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.PHONE, new org.apache.thrift.meta_data.FieldMetaData("phone", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LAST_LOGGED_IN, new org.apache.thrift.meta_data.FieldMetaData("lastLoggedIn", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ACTIVE, new org.apache.thrift.meta_data.FieldMetaData("active", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.NEWSLETTER, new org.apache.thrift.meta_data.FieldMetaData("newsletter", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.REGISTERED, new org.apache.thrift.meta_data.FieldMetaData("registered", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(UserThrift.class, metaDataMap);
  }

  public UserThrift() {
  }

  public UserThrift(
    String emailId)
  {
    this();
    this.emailId = emailId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public UserThrift(UserThrift other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetEmailId()) {
      this.emailId = other.emailId;
    }
    if (other.isSetName()) {
      this.name = other.name;
    }
    if (other.isSetPassword()) {
      this.password = other.password;
    }
    if (other.isSetCountry()) {
      this.country = other.country;
    }
    if (other.isSetPhone()) {
      this.phone = other.phone;
    }
    if (other.isSetLastLoggedIn()) {
      this.lastLoggedIn = other.lastLoggedIn;
    }
    this.active = other.active;
    this.newsletter = other.newsletter;
    this.registered = other.registered;
  }

  public UserThrift deepCopy() {
    return new UserThrift(this);
  }

  @Override
  public void clear() {
    this.emailId = null;
    this.name = null;
    this.password = null;
    this.country = null;
    this.phone = null;
    this.lastLoggedIn = null;
    setActiveIsSet(false);
    this.active = false;
    setNewsletterIsSet(false);
    this.newsletter = false;
    setRegisteredIsSet(false);
    this.registered = false;
  }

  public String getEmailId() {
    return this.emailId;
  }

  public UserThrift setEmailId(String emailId) {
    this.emailId = emailId;
    return this;
  }

  public void unsetEmailId() {
    this.emailId = null;
  }

  /** Returns true if field emailId is set (has been assigned a value) and false otherwise */
  public boolean isSetEmailId() {
    return this.emailId != null;
  }

  public void setEmailIdIsSet(boolean value) {
    if (!value) {
      this.emailId = null;
    }
  }

  public String getName() {
    return this.name;
  }

  public UserThrift setName(String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been assigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public String getPassword() {
    return this.password;
  }

  public UserThrift setPassword(String password) {
    this.password = password;
    return this;
  }

  public void unsetPassword() {
    this.password = null;
  }

  /** Returns true if field password is set (has been assigned a value) and false otherwise */
  public boolean isSetPassword() {
    return this.password != null;
  }

  public void setPasswordIsSet(boolean value) {
    if (!value) {
      this.password = null;
    }
  }

  public String getCountry() {
    return this.country;
  }

  public UserThrift setCountry(String country) {
    this.country = country;
    return this;
  }

  public void unsetCountry() {
    this.country = null;
  }

  /** Returns true if field country is set (has been assigned a value) and false otherwise */
  public boolean isSetCountry() {
    return this.country != null;
  }

  public void setCountryIsSet(boolean value) {
    if (!value) {
      this.country = null;
    }
  }

  public String getPhone() {
    return this.phone;
  }

  public UserThrift setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public void unsetPhone() {
    this.phone = null;
  }

  /** Returns true if field phone is set (has been assigned a value) and false otherwise */
  public boolean isSetPhone() {
    return this.phone != null;
  }

  public void setPhoneIsSet(boolean value) {
    if (!value) {
      this.phone = null;
    }
  }

  public String getLastLoggedIn() {
    return this.lastLoggedIn;
  }

  public UserThrift setLastLoggedIn(String lastLoggedIn) {
    this.lastLoggedIn = lastLoggedIn;
    return this;
  }

  public void unsetLastLoggedIn() {
    this.lastLoggedIn = null;
  }

  /** Returns true if field lastLoggedIn is set (has been assigned a value) and false otherwise */
  public boolean isSetLastLoggedIn() {
    return this.lastLoggedIn != null;
  }

  public void setLastLoggedInIsSet(boolean value) {
    if (!value) {
      this.lastLoggedIn = null;
    }
  }

  public boolean isActive() {
    return this.active;
  }

  public UserThrift setActive(boolean active) {
    this.active = active;
    setActiveIsSet(true);
    return this;
  }

  public void unsetActive() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ACTIVE_ISSET_ID);
  }

  /** Returns true if field active is set (has been assigned a value) and false otherwise */
  public boolean isSetActive() {
    return EncodingUtils.testBit(__isset_bitfield, __ACTIVE_ISSET_ID);
  }

  public void setActiveIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ACTIVE_ISSET_ID, value);
  }

  public boolean isNewsletter() {
    return this.newsletter;
  }

  public UserThrift setNewsletter(boolean newsletter) {
    this.newsletter = newsletter;
    setNewsletterIsSet(true);
    return this;
  }

  public void unsetNewsletter() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __NEWSLETTER_ISSET_ID);
  }

  /** Returns true if field newsletter is set (has been assigned a value) and false otherwise */
  public boolean isSetNewsletter() {
    return EncodingUtils.testBit(__isset_bitfield, __NEWSLETTER_ISSET_ID);
  }

  public void setNewsletterIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __NEWSLETTER_ISSET_ID, value);
  }

  public boolean isRegistered() {
    return this.registered;
  }

  public UserThrift setRegistered(boolean registered) {
    this.registered = registered;
    setRegisteredIsSet(true);
    return this;
  }

  public void unsetRegistered() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __REGISTERED_ISSET_ID);
  }

  /** Returns true if field registered is set (has been assigned a value) and false otherwise */
  public boolean isSetRegistered() {
    return EncodingUtils.testBit(__isset_bitfield, __REGISTERED_ISSET_ID);
  }

  public void setRegisteredIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __REGISTERED_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case EMAIL_ID:
      if (value == null) {
        unsetEmailId();
      } else {
        setEmailId((String)value);
      }
      break;

    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((String)value);
      }
      break;

    case PASSWORD:
      if (value == null) {
        unsetPassword();
      } else {
        setPassword((String)value);
      }
      break;

    case COUNTRY:
      if (value == null) {
        unsetCountry();
      } else {
        setCountry((String)value);
      }
      break;

    case PHONE:
      if (value == null) {
        unsetPhone();
      } else {
        setPhone((String)value);
      }
      break;

    case LAST_LOGGED_IN:
      if (value == null) {
        unsetLastLoggedIn();
      } else {
        setLastLoggedIn((String)value);
      }
      break;

    case ACTIVE:
      if (value == null) {
        unsetActive();
      } else {
        setActive((Boolean)value);
      }
      break;

    case NEWSLETTER:
      if (value == null) {
        unsetNewsletter();
      } else {
        setNewsletter((Boolean)value);
      }
      break;

    case REGISTERED:
      if (value == null) {
        unsetRegistered();
      } else {
        setRegistered((Boolean)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case EMAIL_ID:
      return getEmailId();

    case NAME:
      return getName();

    case PASSWORD:
      return getPassword();

    case COUNTRY:
      return getCountry();

    case PHONE:
      return getPhone();

    case LAST_LOGGED_IN:
      return getLastLoggedIn();

    case ACTIVE:
      return Boolean.valueOf(isActive());

    case NEWSLETTER:
      return Boolean.valueOf(isNewsletter());

    case REGISTERED:
      return Boolean.valueOf(isRegistered());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case EMAIL_ID:
      return isSetEmailId();
    case NAME:
      return isSetName();
    case PASSWORD:
      return isSetPassword();
    case COUNTRY:
      return isSetCountry();
    case PHONE:
      return isSetPhone();
    case LAST_LOGGED_IN:
      return isSetLastLoggedIn();
    case ACTIVE:
      return isSetActive();
    case NEWSLETTER:
      return isSetNewsletter();
    case REGISTERED:
      return isSetRegistered();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof UserThrift)
      return this.equals((UserThrift)that);
    return false;
  }

  public boolean equals(UserThrift that) {
    if (that == null)
      return false;

    boolean this_present_emailId = true && this.isSetEmailId();
    boolean that_present_emailId = true && that.isSetEmailId();
    if (this_present_emailId || that_present_emailId) {
      if (!(this_present_emailId && that_present_emailId))
        return false;
      if (!this.emailId.equals(that.emailId))
        return false;
    }

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_password = true && this.isSetPassword();
    boolean that_present_password = true && that.isSetPassword();
    if (this_present_password || that_present_password) {
      if (!(this_present_password && that_present_password))
        return false;
      if (!this.password.equals(that.password))
        return false;
    }

    boolean this_present_country = true && this.isSetCountry();
    boolean that_present_country = true && that.isSetCountry();
    if (this_present_country || that_present_country) {
      if (!(this_present_country && that_present_country))
        return false;
      if (!this.country.equals(that.country))
        return false;
    }

    boolean this_present_phone = true && this.isSetPhone();
    boolean that_present_phone = true && that.isSetPhone();
    if (this_present_phone || that_present_phone) {
      if (!(this_present_phone && that_present_phone))
        return false;
      if (!this.phone.equals(that.phone))
        return false;
    }

    boolean this_present_lastLoggedIn = true && this.isSetLastLoggedIn();
    boolean that_present_lastLoggedIn = true && that.isSetLastLoggedIn();
    if (this_present_lastLoggedIn || that_present_lastLoggedIn) {
      if (!(this_present_lastLoggedIn && that_present_lastLoggedIn))
        return false;
      if (!this.lastLoggedIn.equals(that.lastLoggedIn))
        return false;
    }

    boolean this_present_active = true && this.isSetActive();
    boolean that_present_active = true && that.isSetActive();
    if (this_present_active || that_present_active) {
      if (!(this_present_active && that_present_active))
        return false;
      if (this.active != that.active)
        return false;
    }

    boolean this_present_newsletter = true && this.isSetNewsletter();
    boolean that_present_newsletter = true && that.isSetNewsletter();
    if (this_present_newsletter || that_present_newsletter) {
      if (!(this_present_newsletter && that_present_newsletter))
        return false;
      if (this.newsletter != that.newsletter)
        return false;
    }

    boolean this_present_registered = true && this.isSetRegistered();
    boolean that_present_registered = true && that.isSetRegistered();
    if (this_present_registered || that_present_registered) {
      if (!(this_present_registered && that_present_registered))
        return false;
      if (this.registered != that.registered)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(UserThrift other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    UserThrift typedOther = (UserThrift)other;

    lastComparison = Boolean.valueOf(isSetEmailId()).compareTo(typedOther.isSetEmailId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEmailId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.emailId, typedOther.emailId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetName()).compareTo(typedOther.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, typedOther.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPassword()).compareTo(typedOther.isSetPassword());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPassword()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.password, typedOther.password);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCountry()).compareTo(typedOther.isSetCountry());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCountry()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.country, typedOther.country);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetPhone()).compareTo(typedOther.isSetPhone());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPhone()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.phone, typedOther.phone);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetLastLoggedIn()).compareTo(typedOther.isSetLastLoggedIn());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLastLoggedIn()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lastLoggedIn, typedOther.lastLoggedIn);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetActive()).compareTo(typedOther.isSetActive());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetActive()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.active, typedOther.active);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetNewsletter()).compareTo(typedOther.isSetNewsletter());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetNewsletter()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.newsletter, typedOther.newsletter);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetRegistered()).compareTo(typedOther.isSetRegistered());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRegistered()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.registered, typedOther.registered);
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
    StringBuilder sb = new StringBuilder("UserThrift(");
    boolean first = true;

    sb.append("emailId:");
    if (this.emailId == null) {
      sb.append("null");
    } else {
      sb.append(this.emailId);
    }
    first = false;
    if (isSetName()) {
      if (!first) sb.append(", ");
      sb.append("name:");
      if (this.name == null) {
        sb.append("null");
      } else {
        sb.append(this.name);
      }
      first = false;
    }
    if (isSetPassword()) {
      if (!first) sb.append(", ");
      sb.append("password:");
      if (this.password == null) {
        sb.append("null");
      } else {
        sb.append(this.password);
      }
      first = false;
    }
    if (isSetCountry()) {
      if (!first) sb.append(", ");
      sb.append("country:");
      if (this.country == null) {
        sb.append("null");
      } else {
        sb.append(this.country);
      }
      first = false;
    }
    if (isSetPhone()) {
      if (!first) sb.append(", ");
      sb.append("phone:");
      if (this.phone == null) {
        sb.append("null");
      } else {
        sb.append(this.phone);
      }
      first = false;
    }
    if (isSetLastLoggedIn()) {
      if (!first) sb.append(", ");
      sb.append("lastLoggedIn:");
      if (this.lastLoggedIn == null) {
        sb.append("null");
      } else {
        sb.append(this.lastLoggedIn);
      }
      first = false;
    }
    if (isSetActive()) {
      if (!first) sb.append(", ");
      sb.append("active:");
      sb.append(this.active);
      first = false;
    }
    if (isSetNewsletter()) {
      if (!first) sb.append(", ");
      sb.append("newsletter:");
      sb.append(this.newsletter);
      first = false;
    }
    if (isSetRegistered()) {
      if (!first) sb.append(", ");
      sb.append("registered:");
      sb.append(this.registered);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (emailId == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'emailId' was not present! Struct: " + toString());
    }
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

  private static class UserThriftStandardSchemeFactory implements SchemeFactory {
    public UserThriftStandardScheme getScheme() {
      return new UserThriftStandardScheme();
    }
  }

  private static class UserThriftStandardScheme extends StandardScheme<UserThrift> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, UserThrift struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // EMAIL_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.emailId = iprot.readString();
              struct.setEmailIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.name = iprot.readString();
              struct.setNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // PASSWORD
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.password = iprot.readString();
              struct.setPasswordIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // COUNTRY
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.country = iprot.readString();
              struct.setCountryIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // PHONE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.phone = iprot.readString();
              struct.setPhoneIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // LAST_LOGGED_IN
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.lastLoggedIn = iprot.readString();
              struct.setLastLoggedInIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // ACTIVE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.active = iprot.readBool();
              struct.setActiveIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // NEWSLETTER
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.newsletter = iprot.readBool();
              struct.setNewsletterIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 9: // REGISTERED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.registered = iprot.readBool();
              struct.setRegisteredIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, UserThrift struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.emailId != null) {
        oprot.writeFieldBegin(EMAIL_ID_FIELD_DESC);
        oprot.writeString(struct.emailId);
        oprot.writeFieldEnd();
      }
      if (struct.name != null) {
        if (struct.isSetName()) {
          oprot.writeFieldBegin(NAME_FIELD_DESC);
          oprot.writeString(struct.name);
          oprot.writeFieldEnd();
        }
      }
      if (struct.password != null) {
        if (struct.isSetPassword()) {
          oprot.writeFieldBegin(PASSWORD_FIELD_DESC);
          oprot.writeString(struct.password);
          oprot.writeFieldEnd();
        }
      }
      if (struct.country != null) {
        if (struct.isSetCountry()) {
          oprot.writeFieldBegin(COUNTRY_FIELD_DESC);
          oprot.writeString(struct.country);
          oprot.writeFieldEnd();
        }
      }
      if (struct.phone != null) {
        if (struct.isSetPhone()) {
          oprot.writeFieldBegin(PHONE_FIELD_DESC);
          oprot.writeString(struct.phone);
          oprot.writeFieldEnd();
        }
      }
      if (struct.lastLoggedIn != null) {
        if (struct.isSetLastLoggedIn()) {
          oprot.writeFieldBegin(LAST_LOGGED_IN_FIELD_DESC);
          oprot.writeString(struct.lastLoggedIn);
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetActive()) {
        oprot.writeFieldBegin(ACTIVE_FIELD_DESC);
        oprot.writeBool(struct.active);
        oprot.writeFieldEnd();
      }
      if (struct.isSetNewsletter()) {
        oprot.writeFieldBegin(NEWSLETTER_FIELD_DESC);
        oprot.writeBool(struct.newsletter);
        oprot.writeFieldEnd();
      }
      if (struct.isSetRegistered()) {
        oprot.writeFieldBegin(REGISTERED_FIELD_DESC);
        oprot.writeBool(struct.registered);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class UserThriftTupleSchemeFactory implements SchemeFactory {
    public UserThriftTupleScheme getScheme() {
      return new UserThriftTupleScheme();
    }
  }

  private static class UserThriftTupleScheme extends TupleScheme<UserThrift> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, UserThrift struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.emailId);
      BitSet optionals = new BitSet();
      if (struct.isSetName()) {
        optionals.set(0);
      }
      if (struct.isSetPassword()) {
        optionals.set(1);
      }
      if (struct.isSetCountry()) {
        optionals.set(2);
      }
      if (struct.isSetPhone()) {
        optionals.set(3);
      }
      if (struct.isSetLastLoggedIn()) {
        optionals.set(4);
      }
      if (struct.isSetActive()) {
        optionals.set(5);
      }
      if (struct.isSetNewsletter()) {
        optionals.set(6);
      }
      if (struct.isSetRegistered()) {
        optionals.set(7);
      }
      oprot.writeBitSet(optionals, 8);
      if (struct.isSetName()) {
        oprot.writeString(struct.name);
      }
      if (struct.isSetPassword()) {
        oprot.writeString(struct.password);
      }
      if (struct.isSetCountry()) {
        oprot.writeString(struct.country);
      }
      if (struct.isSetPhone()) {
        oprot.writeString(struct.phone);
      }
      if (struct.isSetLastLoggedIn()) {
        oprot.writeString(struct.lastLoggedIn);
      }
      if (struct.isSetActive()) {
        oprot.writeBool(struct.active);
      }
      if (struct.isSetNewsletter()) {
        oprot.writeBool(struct.newsletter);
      }
      if (struct.isSetRegistered()) {
        oprot.writeBool(struct.registered);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, UserThrift struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.emailId = iprot.readString();
      struct.setEmailIdIsSet(true);
      BitSet incoming = iprot.readBitSet(8);
      if (incoming.get(0)) {
        struct.name = iprot.readString();
        struct.setNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.password = iprot.readString();
        struct.setPasswordIsSet(true);
      }
      if (incoming.get(2)) {
        struct.country = iprot.readString();
        struct.setCountryIsSet(true);
      }
      if (incoming.get(3)) {
        struct.phone = iprot.readString();
        struct.setPhoneIsSet(true);
      }
      if (incoming.get(4)) {
        struct.lastLoggedIn = iprot.readString();
        struct.setLastLoggedInIsSet(true);
      }
      if (incoming.get(5)) {
        struct.active = iprot.readBool();
        struct.setActiveIsSet(true);
      }
      if (incoming.get(6)) {
        struct.newsletter = iprot.readBool();
        struct.setNewsletterIsSet(true);
      }
      if (incoming.get(7)) {
        struct.registered = iprot.readBool();
        struct.setRegisteredIsSet(true);
      }
    }
  }

}

