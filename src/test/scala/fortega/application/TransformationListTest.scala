package fortega.application

import org.scalatest.flatspec.AnyFlatSpec

class TransformationListTest extends AnyFlatSpec {
    import fortega.Utils.data

    "TransformationList" should "store 3 transformations" in {
        val list = TransformationList()
        assert(list.size == 3)

        assert(list.map(_.name) == List("position table", "shot effectiveness", "goals against"))
    }

    it should "run on data without errors" in {
        TransformationList()
            .map(_.process)
            .foreach { data.transform(_).show }
        succeed
    }
}
