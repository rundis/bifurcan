package io.lacuna.bifurcan;

import io.lacuna.bifurcan.durable.Bytes;
import io.lacuna.bifurcan.durable.Util;
import io.lacuna.bifurcan.durable.io.BufferInput;
import io.lacuna.bifurcan.durable.io.DurableBuffer;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public interface IDurableCollection {

  interface Fingerprint extends Comparable<Fingerprint> {
    byte[] binary();

    default String toHexString() {
      return Bytes.toHexString(ByteBuffer.wrap(binary()));
    }

    default int compareTo(Fingerprint o) {
      return Bytes.compareBuffers(
          ByteBuffer.wrap(binary()),
          ByteBuffer.wrap(o.binary()));
    }
  }

  /**
   *
   */
  interface Root {
    void close();

    Path path();

    DurableInput.Pool bytes();

    Fingerprint fingerprint();

    DurableInput cached(DurableInput in);

    IMap<Fingerprint, Root> dependencies();
  }

  IDurableEncoding encoding();

  DurableInput.Pool bytes();

  Root root();

  default IList<Fingerprint> dependencies() {
    return null;
  }
}
