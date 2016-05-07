package org.selfconference.android.data;

import org.selfconference.android.data.api.ApiModule;

public enum ApiEndpoints {
  PRODUCTION("Production", ApiModule.PRODUCTION_API_URL.toString());
  // STAGING("Staging", "http://selfconf-dev.herokuapp.com/api/"),
  // MOCK_MODE("Mock Mode", "http://localhost/mock/");

  public final String name;
  public final String url;

  ApiEndpoints(String name, String url) {
    this.name = name;
    this.url = url;
  }

  @Override public String toString() {
    return name;
  }

  public static ApiEndpoints from(String endpoint) {
    for (ApiEndpoints value : values()) {
      if (value.url.equals(endpoint)) {
        return value;
      }
    }
    throw new IllegalArgumentException("Invalid endpoint");
  }

  public static boolean isMockMode(String endpoint) {
    return false;
    // return from(endpoint) == MOCK_MODE;
  }
}
