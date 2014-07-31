package com.stevewedig.blog.value_objects;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import static com.stevewedig.blog.value_objects.CompareLib.assertEqualObjectsAndStrings;
import static com.stevewedig.blog.value_objects.CompareLib.assertUnequalObjectsAndStrings;

public class TestMixinDetails {

  // ===========================================================================
  // helpers should be cached on values and not cached on entities
  // ===========================================================================

  @Test
  public void testHelperCachedOnValues() {

    ValueWithCounter value = new ValueWithCounter();

    // helper is cached
    assertThat(value.objectHelper(), sameInstance(value.objectHelper()));

    assertEquals(value.hashCounter.hashCount, 0);

    value.hashCode();
    assertEquals(value.hashCounter.hashCount, 1);

    // count stays at 1 forever
    value.hashCode();
    assertEquals(value.hashCounter.hashCount, 1);
    value.hashCode();
    assertEquals(value.hashCounter.hashCount, 1);
  }

  @Test
  public void testHelperNotCachedOnEntities() {

    EntityWithCounter entity = new EntityWithCounter();

    // helper not cached
    assertThat(entity.objectHelper(), not(sameInstance(entity.objectHelper())));

    assertEquals(entity.hashCounter.hashCount, 0);

    // nested object shouldn't be hashed
    entity.hashCode();
    assertEquals(entity.hashCounter.hashCount, 0);
  }

  private static class HashCounter {

    public int hashCount = 0;

    @Override
    public int hashCode() {
      hashCount++;
      return super.hashCode();
    }
  }

  private static class ValueWithCounter extends ValueMixin {
    public HashCounter hashCounter = new HashCounter();

    @Override
    public Object[] fields() {
      return array("hashCounter", hashCounter);
    }
  }

  private static class EntityWithCounter extends EntityMixin {
    public HashCounter hashCounter = new HashCounter();

    @Override
    public Object[] fields() {
      return array("hashCounter", hashCounter);
    }
  }

  // ===========================================================================
  // making sure behavior (classes) are checked as well as state (fields)
  // ===========================================================================

  @Test
  public void testBehaviorChecking() {

    // same state (fields) but different behavior (classes)
    A a = new A("val");
    B b = new B("val");

    CompareLib.assertUnequalObjectsAndStrings(a, b);
  }

  private static class A extends ValueMixin {

    private final String name;

    public A(String name) {
      this.name = name;
    }

    @Override
    protected Object[] fields() {
      return array("name", name);
    }
  }

  private static class B extends ValueMixin {

    private final String name;

    public B(String name) {
      this.name = name;
    }

    @Override
    protected Object[] fields() {
      return array("name", name);
    }
  }


  // ===========================================================================
  // verify that value mixin works nested entities
  // (see the blog post for why I don't personally do this)
  // ===========================================================================

  @Test
  public void testValuesCanContainEntities() {

    Object entity1 = new Object();
    Object entity2 = new Object();

    assertEqualObjectsAndStrings(new C(true, entity1), new C(true, entity1));

    assertUnequalObjectsAndStrings(new C(true, entity1), new C(false, entity1));

    assertUnequalObjectsAndStrings(new C(true, entity1), new C(true, entity2));
  }


  private static class C extends ValueMixin {

    private final boolean myFlag;
    private final Object myEntity;

    public C(boolean myFlag, Object myEntity) {
      this.myFlag = myFlag;
      this.myEntity = myEntity;
    }

    @Override
    protected Object[] fields() {
      return array("myFlag", myFlag, "myEntity", myEntity);
    }
  }

}
