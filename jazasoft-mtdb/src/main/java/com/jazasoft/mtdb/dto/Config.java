package com.jazasoft.mtdb.dto;

import lombok.Data;

/**
 * Created by mdzahidraza on 15/12/17.
 */
@Data
public class Config {
  private String label;
  private String key;
  private String value;
  private String unit;
  private String type; //e.g String|number|boolean
}
