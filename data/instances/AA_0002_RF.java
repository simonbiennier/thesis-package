package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.apache.commons.lang3.Functions.FailableBiConsumer;
import org.apache.commons.lang3.Functions.FailableBiFunction;
import org.apache.commons.lang3.Functions.FailableCallable;
import org.apache.commons.lang3.Functions.FailableConsumer;
import org.apache.commons.lang3.Functions.FailableFunction;
import org.apache.commons.lang3.Functions.FailableSupplier;
import org.apache.commons.lang3.Functions.FailableBiPredicate;
import org.apache.commons.lang3.Functions.FailablePredicate;
import org.apache.commons.lang3.Functions.FailableRunnable;
import org.junit.jupiter.api.Test;

/**
 * Tests Functions.
 *
 * @deprecated this test can be removed once the deprecated source class
 *             {@link org.apache.commons.lang3.Functions} is removed.
 */
class AA_0002_RF {
  @Test
  void testConstructor() {
    Functions functions = new Functions();
    assertNotNull(functions);
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableBiConsumer_Object_Throwable() {
    final FailableBiConsumer<Object, Object, Throwable> consumer = new FailableBiConsumer<Object, Object, Throwable>() {
      @Override
      public void accept(final Object object1, final Object object2) throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> consumer.accept(null, null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableBiConsumer_String_IOException() {
    final FailableBiConsumer<String, String, IOException> consumer = new FailableBiConsumer<String, String, IOException>() {
      @Override
      public void accept(final String object1, final String object2) throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> consumer.accept(null, null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableBiFunction_Object_Throwable() {
    final FailableBiFunction<Object, Object, Object, Throwable> function = new FailableBiFunction<Object, Object, Object, Throwable>() {
      @Override
      public Object apply(final Object input1, final Object input2) throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> function.apply(null, null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableBiFunction_String_IOException() {
    final FailableBiFunction<String, String, String, IOException> function = new FailableBiFunction<String, String, String, IOException>() {
      @Override
      public String apply(final String input1, final String input2) throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> function.apply(null, null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableBiPredicate_Object_Throwable() {
    final FailableBiPredicate<Object, Object, Throwable> predicate = new FailableBiPredicate<Object, Object, Throwable>() {
      @Override
      public boolean test(final Object object1, final Object object2) throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> predicate.test(null, null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableBiPredicate_String_IOException() {
    final FailableBiPredicate<String, String, IOException> predicate = new FailableBiPredicate<String, String, IOException>() {
      @Override
      public boolean test(final String object1, final String object2) throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> predicate.test(null, null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableCallable_Object_Throwable() {
    final FailableCallable<Object, Throwable> callable = new FailableCallable<Object, Throwable>() {
      @Override
      public Object call() throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> callable.call());
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableCallable_String_IOException() {
    final FailableCallable<String, IOException> callable = new FailableCallable<String, IOException>() {
      @Override
      public String call() throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> callable.call());
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableConsumer_Object_Throwable() {
    final FailableConsumer<Object, Throwable> consumer = new FailableConsumer<Object, Throwable>() {
      @Override
      public void accept(final Object object) throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> consumer.accept(null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableConsumer_String_IOException() {
    final FailableConsumer<String, IOException> consumer = new FailableConsumer<String, IOException>() {
      @Override
      public void accept(final String object) throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> consumer.accept(null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableFunction_Object_Throwable() {
    final FailableFunction<Object, Object, Throwable> function = new FailableFunction<Object, Object, Throwable>() {
      @Override
      public Object apply(final Object input) throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> function.apply(null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableFunction_String_IOException() {
    final FailableFunction<String, String, IOException> function = new FailableFunction<String, String, IOException>() {
      @Override
      public String apply(final String input) throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> function.apply(null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailablePredicate_Object_Throwable() {
    final FailablePredicate<Object, Throwable> predicate = new FailablePredicate<Object, Throwable>() {
      @Override
      public boolean test(final Object object) throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> predicate.test(null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailablePredicate_String_IOException() {
    final FailablePredicate<String, IOException> predicate = new FailablePredicate<String, IOException>() {
      @Override
      public boolean test(final String object) throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> predicate.test(null));
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableRunnable_Object_Throwable() {
    final FailableRunnable<Throwable> runnable = new FailableRunnable<Throwable>() {
      @Override
      public void run() throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> runnable.run());
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableRunnable_String_IOException() {
    final FailableRunnable<IOException> runnable = new FailableRunnable<IOException>() {
      @Override
      public void run() throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> runnable.run());
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableSupplier_Object_Throwable() {
    final FailableSupplier<Object, Throwable> supplier = new FailableSupplier<Object, Throwable>() {
      @Override
      public Object get() throws Throwable {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> supplier.get());
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableSupplier_String_IOException() {
    final FailableSupplier<String, IOException> supplier = new FailableSupplier<String, IOException>() {
      @Override
      public String get() throws IOException {
        throw new IOException("test");
      }
    };
    assertThrows(IOException.class, () -> supplier.get());
  }
}
