package io.lacuna.bifurcan.durable;

import io.lacuna.bifurcan.*;
import io.lacuna.bifurcan.IDurableCollection.Fingerprint;
import java.util.stream.LongStream;

public class Dependencies {

  private static ThreadLocal<LinearSet<Fingerprint>> ROOT_DEPENDENCIES = ThreadLocal.withInitial(LinearSet::new);

  public static void enter() {
    ROOT_DEPENDENCIES.set(new LinearSet<>());
  }

  public static void add(IDurableCollection.Root root) {
    ROOT_DEPENDENCIES.get().add(root.fingerprint());
  }

  public static ISet<Fingerprint> exit() {
    return ROOT_DEPENDENCIES.get();
  }
  
  public static void encode(ISet<Fingerprint> dependencies, DurableOutput out) {
    out.writeUnsignedInt(dependencies.size());
    dependencies.forEach(f -> Fingerprints.encode(f, out));
  }

  public static ISet<Fingerprint> decode(DurableInput in) {
    return LongStream.range(0, in.readUnsignedInt())
        .mapToObj(n -> Fingerprints.decode(in))
        .collect(Sets.linearCollector())
        .forked();
  }
}
