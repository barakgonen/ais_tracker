package com.example.application.events;

import org.springframework.context.ApplicationEvent;

import com.ais.avro.schemas.RawData;

import lombok.Getter;

@Getter
public class RawDataEvent extends ApplicationEvent {
  private RawData rawData;
  private String msgSource;

  public RawDataEvent(Object source, RawData rawData, String origin) {
    super(source);
    this.rawData = rawData;
    this.msgSource = origin;
  }
}
