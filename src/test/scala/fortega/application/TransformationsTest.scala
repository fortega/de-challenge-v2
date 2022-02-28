package fortega.application

import org.scalatest.flatspec.AnyFlatSpec

class TransformationsTest extends AnyFlatSpec {
    import fortega.Utils.data

    "PositionTable" should "return two results" in {
        val transformation = PositionTableTransformation()
        val result = data.transform(transformation.process)

        assert(result.count == 2)
    }

    "ShotEffectiveness" should "return one result" in {
        val transformation = ShotEffectivenessTransformation()
        val result = data.transform(transformation.process)

        assert(result.count == 1)
    }

    "MoreGoalsAgainst" should "return one result" in {
        val transformation = GoalsAgainstTransformation()
        val result = data.transform(transformation.process)

        assert(result.count == 1)
    }
}
