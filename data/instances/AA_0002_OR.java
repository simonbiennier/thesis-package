package org.apache.commons.lang3;

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
class AA_0002_OR {
  @Test
  void testConstructor() {
    // We allow this, which must have been an omission to make the ctor private.
    // We could make the ctor private in 4.0.
    new Functions();
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableBiConsumer_Object_Throwable() {
    new FailableBiConsumer<Object, Object, Throwable>() {
      @Override
      public void accept(final Object object1, final Object object2) throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableBiConsumer_String_IOException() {
    new FailableBiConsumer<String, String, IOException>() {
      @Override
      public void accept(final String object1, final String object2) throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableBiFunction_Object_Throwable() {
    new FailableBiFunction<Object, Object, Object, Throwable>() {
      @Override
      public Object apply(final Object input1, final Object input2) throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableBiFunction_String_IOException() {
    new FailableBiFunction<String, String, String, IOException>() {
      @Override
      public String apply(final String input1, final String input2) throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableBiPredicate_Object_Throwable() {
    new FailableBiPredicate<Object, Object, Throwable>() {
      @Override
      public boolean test(final Object object1, final Object object2) throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableBiPredicate_String_IOException() {
    new FailableBiPredicate<String, String, IOException>() {
      @Override
      public boolean test(final String object1, final String object2) throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableCallable_Object_Throwable() {
    new FailableCallable<Object, Throwable>() {
      @Override
      public Object call() throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableCallable_String_IOException() {
    new FailableCallable<String, IOException>() {
      @Override
      public String call() throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableConsumer_Object_Throwable() {
    new FailableConsumer<Object, Throwable>() {
      @Override
      public void accept(final Object object) throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableConsumer_String_IOException() {
    new FailableConsumer<String, IOException>() {
      @Override
      public void accept(final String object) throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableFunction_Object_Throwable() {
    new FailableFunction<Object, Object, Throwable>() {
      @Override
      public Object apply(final Object input) throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableFunction_String_IOException() {
    new FailableFunction<String, String, IOException>() {
      @Override
      public String apply(final String input) throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailablePredicate_Object_Throwable() {
    new FailablePredicate<Object, Throwable>() {
      @Override
      public boolean test(final Object object) throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailablePredicate_String_IOException() {
    new FailablePredicate<String, IOException>() {
      @Override
      public boolean test(final String object) throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableRunnable_Object_Throwable() {
    new FailableRunnable<Throwable>() {
      @Override
      public void run() throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableRunnable_String_IOException() {
    new FailableRunnable<IOException>() {
      @Override
      public void run() throws IOException {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception.
   * using the top level generic types
   * Object and Throwable.
   */
  @Test
  void testThrows_FailableSupplier_Object_Throwable() {
    new FailableSupplier<Object, Throwable>() {
      @Override
      public Object get() throws Throwable {
        throw new IOException("test");
      }
    };
  }

  /**
   * Tests that our failable interface is properly defined to throw any exception
   * using String and IOExceptions as
   * generic test types.
   */
  @Test
  void testThrows_FailableSupplier_String_IOException() {
    new FailableSupplier<String, IOException>() {
      @Override
      public String get() throws IOException {
        throw new IOException("test");
      }
    };
  }
}
