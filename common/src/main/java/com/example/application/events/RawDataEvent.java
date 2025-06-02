package com.example.application.events;

import org.springframework.context.ApplicationEvent;

import com.ais.avro.schemas.RawData;

import lombok.Getter;

@Getter
public class RawDataEvent extends ApplicationEvent {
  private RawData rawData;

  public RawDataEvent(Object source, RawData rawData) {
    super(source);
    this.rawData = rawData;
  }
}
