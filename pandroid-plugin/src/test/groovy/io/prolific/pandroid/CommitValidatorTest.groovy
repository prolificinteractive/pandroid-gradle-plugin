package io.prolific.pandroid

import io.prolific.pandroid.vcs.CommitValidator

class CommitValidatorTest extends GroovyTestCase {
  void testIsCommitValid() {
    assertTrue(CommitValidator.isCommitValid("abc123 Feature - Home"))
    assertTrue(CommitValidator.isCommitValid("abc123 Feature - Home Screen"))
    assertTrue(CommitValidator.isCommitValid("abc123 Fix - Home"))
    assertTrue(CommitValidator.isCommitValid("abc123 Fix - Home Screen"))
    assertTrue(CommitValidator.isCommitValid("abc123 Chore - Home"))
    assertTrue(CommitValidator.isCommitValid("abc123 Chore - Home Screen"))
    assertTrue(CommitValidator.isCommitValid("abc123 Test - Home"))
    assertTrue(CommitValidator.isCommitValid("abc123 Test - Home Screen"))
  }

  void testIsCommitNotValid() {
    assertFalse(CommitValidator.isCommitValid(""))
    assertFalse(CommitValidator.isCommitValid("abc123"))
    assertFalse(CommitValidator.isCommitValid("abc123 WIP"))
    assertFalse(CommitValidator.isCommitValid("abc123 Home Screen"))
  }
}
