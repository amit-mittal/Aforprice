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

public class configConstants {

  public static final List<Integer> CONFIG_SERVER_PORT = new ArrayList<Integer>();
  static {
    CONFIG_SERVER_PORT.add(27001);
    CONFIG_SERVER_PORT.add(27001);
  }

  public static final List<Event> EVENTS = new ArrayList<Event>();
  static {
    Event tmp0 = new Event();
    DateObj tmp1 = new DateObj();
    tmp1.setDate((short)1);
    tmp1.setMonth((short)1);
    tmp1.setYear((short)2013);

    tmp0.setEventDate(tmp1);
    tmp0.setDescription("New Year's Day");

    EVENTS.add(tmp0);
    Event tmp2 = new Event();
    DateObj tmp3 = new DateObj();
    tmp3.setDate((short)21);
    tmp3.setMonth((short)1);
    tmp3.setYear((short)2013);

    tmp2.setEventDate(tmp3);
    tmp2.setDescription("Martin Luther King Day");

    EVENTS.add(tmp2);
    Event tmp4 = new Event();
    DateObj tmp5 = new DateObj();
    tmp5.setDate((short)14);
    tmp5.setMonth((short)2);
    tmp5.setYear((short)2013);

    tmp4.setEventDate(tmp5);
    tmp4.setDescription("Valentine's Day");

    EVENTS.add(tmp4);
    Event tmp6 = new Event();
    DateObj tmp7 = new DateObj();
    tmp7.setDate((short)18);
    tmp7.setMonth((short)2);
    tmp7.setYear((short)2013);

    tmp6.setEventDate(tmp7);
    tmp6.setDescription("Presidents' Day (Washington's Birthday)");

    EVENTS.add(tmp6);
    Event tmp8 = new Event();
    DateObj tmp9 = new DateObj();
    tmp9.setDate((short)12);
    tmp9.setMonth((short)5);
    tmp9.setYear((short)2013);

    tmp8.setEventDate(tmp9);
    tmp8.setDescription("Mother's Day");

    EVENTS.add(tmp8);
    Event tmp10 = new Event();
    DateObj tmp11 = new DateObj();
    tmp11.setDate((short)27);
    tmp11.setMonth((short)5);
    tmp11.setYear((short)2013);

    tmp10.setEventDate(tmp11);
    tmp10.setDescription("Memorial Day");

    EVENTS.add(tmp10);
    Event tmp12 = new Event();
    DateObj tmp13 = new DateObj();
    tmp13.setDate((short)16);
    tmp13.setMonth((short)6);
    tmp13.setYear((short)2013);

    tmp12.setEventDate(tmp13);
    tmp12.setDescription("Father's Day");

    EVENTS.add(tmp12);
    Event tmp14 = new Event();
    DateObj tmp15 = new DateObj();
    tmp15.setDate((short)4);
    tmp15.setMonth((short)7);
    tmp15.setYear((short)2013);

    tmp14.setEventDate(tmp15);
    tmp14.setDescription("Independence Day");

    EVENTS.add(tmp14);
    Event tmp16 = new Event();
    DateObj tmp17 = new DateObj();
    tmp17.setDate((short)2);
    tmp17.setMonth((short)9);
    tmp17.setYear((short)2013);

    tmp16.setEventDate(tmp17);
    tmp16.setDescription("Labor Day");

    EVENTS.add(tmp16);
    Event tmp18 = new Event();
    DateObj tmp19 = new DateObj();
    tmp19.setDate((short)14);
    tmp19.setMonth((short)10);
    tmp19.setYear((short)2013);

    tmp18.setEventDate(tmp19);
    tmp18.setDescription("Columbus Day");

    EVENTS.add(tmp18);
    Event tmp20 = new Event();
    DateObj tmp21 = new DateObj();
    tmp21.setDate((short)31);
    tmp21.setMonth((short)10);
    tmp21.setYear((short)2013);

    tmp20.setEventDate(tmp21);
    tmp20.setDescription("Halloween");

    EVENTS.add(tmp20);
    Event tmp22 = new Event();
    DateObj tmp23 = new DateObj();
    tmp23.setDate((short)11);
    tmp23.setMonth((short)11);
    tmp23.setYear((short)2013);

    tmp22.setEventDate(tmp23);
    tmp22.setDescription("Veterans Day");

    EVENTS.add(tmp22);
    Event tmp24 = new Event();
    DateObj tmp25 = new DateObj();
    tmp25.setDate((short)28);
    tmp25.setMonth((short)11);
    tmp25.setYear((short)2013);

    tmp24.setEventDate(tmp25);
    tmp24.setDescription("Thanksgiving Day");

    EVENTS.add(tmp24);
    Event tmp26 = new Event();
    DateObj tmp27 = new DateObj();
    tmp27.setDate((short)24);
    tmp27.setMonth((short)12);
    tmp27.setYear((short)2013);

    tmp26.setEventDate(tmp27);
    tmp26.setDescription("Christmas Eve");

    EVENTS.add(tmp26);
    Event tmp28 = new Event();
    DateObj tmp29 = new DateObj();
    tmp29.setDate((short)25);
    tmp29.setMonth((short)12);
    tmp29.setYear((short)2013);

    tmp28.setEventDate(tmp29);
    tmp28.setDescription("Christmas Day");

    EVENTS.add(tmp28);
    Event tmp30 = new Event();
    DateObj tmp31 = new DateObj();
    tmp31.setDate((short)31);
    tmp31.setMonth((short)12);
    tmp31.setYear((short)2013);

    tmp30.setEventDate(tmp31);
    tmp30.setDescription("New Year's Eve");

    EVENTS.add(tmp30);
  }

}