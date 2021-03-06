package com.stevewedig.blog.value_objects;

import static com.stevewedig.blog.value_objects.CompareLib.*;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.*;

public class TestGuavaValues {

  @Test
  public void testGuavaOptional() {

    Optional<String> bob1 = Optional.of("bob");
    Optional<String> bob2 = Optional.of("bob");
    Optional<String> alice = Optional.of("alice");

    Optional<String> absent1 = Optional.absent();
    Optional<String> absent2 = Optional.absent();

    assertEqualObjectsAndStrings(bob1, bob2);
    assertUnequalObjectsAndStrings(bob1, alice);
    assertUnequalObjectsAndStrings(bob1, absent1);

    // apparently Guava does flywheel for absent, so these are the same instance
    assertThat(absent1, sameInstance(absent2));
  }

  @Test
  public void testGuavaImmutableList() {

    ImmutableList<String> bob1 = ImmutableList.of("bob");
    ImmutableList<String> bob2 = ImmutableList.of("bob");
    ImmutableList<String> alice = ImmutableList.of("alice");

    assertEqualObjectsAndStrings(bob1, bob2);
    assertUnequalObjectsAndStrings(bob1, alice);
  }

  @Test
  public void testGuavaImmutableSet() {

    ImmutableSet<String> bob1 = ImmutableSet.of("bob");
    ImmutableSet<String> bob2 = ImmutableSet.of("bob");
    ImmutableSet<String> alice = ImmutableSet.of("alice");

    assertEqualObjectsAndStrings(bob1, bob2);
    assertUnequalObjectsAndStrings(bob1, alice);
  }

  @Test
  public void testGuavaImmutableMap() {

    ImmutableMap<String, Integer> bob1 = ImmutableMap.of("bob", 1);
    ImmutableMap<String, Integer> bob2 = ImmutableMap.of("bob", 1);
    ImmutableMap<String, Integer> differentKey = ImmutableMap.of("alice", 1);
    ImmutableMap<String, Integer> differentValue = ImmutableMap.of("bob", 2);

    assertEqualObjectsAndStrings(bob1, bob2);
    assertUnequalObjectsAndStrings(bob1, differentKey);
    assertUnequalObjectsAndStrings(bob1, differentValue);
  }

  // presumably Guava's other immutable collections work the same (ImmutableMulitset,
  // ImmutableMultimap, ImmutableBiMap, etc.)

}
