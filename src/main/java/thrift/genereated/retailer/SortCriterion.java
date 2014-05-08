/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.genereated.retailer;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum SortCriterion implements org.apache.thrift.TEnum {
  PRICE_ASC(0),
  PRICE_DESC(1),
  DROP_PERCENTAGE(2),
  BEST_SELLERS(3),
  REVIEW_RATINGS(4);

  private final int value;

  private SortCriterion(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static SortCriterion findByValue(int value) { 
    switch (value) {
      case 0:
        return PRICE_ASC;
      case 1:
        return PRICE_DESC;
      case 2:
        return DROP_PERCENTAGE;
      case 3:
        return BEST_SELLERS;
      case 4:
        return REVIEW_RATINGS;
      default:
        return null;
    }
  }
}