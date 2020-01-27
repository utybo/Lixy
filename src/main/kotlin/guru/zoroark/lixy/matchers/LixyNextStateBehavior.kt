package guru.zoroark.lixy.matchers

import guru.zoroark.lixy.LixyStateLabel

/**
 * Subclasses of this class represent a kind of behavior that can be followed
 * when deciding whether to change state or not, usually after a successful
 * match.
 */
sealed class LixyNextStateBehavior

/**
 * Indicates that the state should not be changed
 */
object LixyNoStateChange : LixyNextStateBehavior()

/**
 * Indicates that the state should be changed to the default state
 */
object LixyGoToDefaultState : LixyNextStateBehavior()

/**
 * Indicates that the state should be changed to the state with [stateLabel] as
 * its label.
 *
 * @property stateLabel The label of the state that should be the next state
 * when following this behavior.
 */
class LixyGoToLabeledState(val stateLabel: LixyStateLabel) :
    LixyNextStateBehavior()