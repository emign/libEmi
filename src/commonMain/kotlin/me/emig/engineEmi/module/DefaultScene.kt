import me.emig.engineEmi.engine
import me.emig.engineEmi.module.SceneTemplate

open class DefaultScene : SceneTemplate() {

    override val viewDidLoad = engine.viewDidLoadBody
    override val viewWillLoad = engine.viewWillLoadBody
    override var canvasElements = engine.canvasElements
    override var bodies = engine.bodies
    override val allScreenElements = engine.allScreenElements
    override var controllers = engine.controllers
    override var map = engine.map
    override var camera = engine.camera


}
