package me.emig.engineEmi

import com.soywiz.klock.milliseconds
import com.soywiz.korge.Korge
import com.soywiz.korge.box2d.WorldView
import com.soywiz.korge.scene.Module
import com.soywiz.korge.view.Camera
import com.soywiz.korge.view.Container
import com.soywiz.korio.file.VfsFile
import com.soywiz.korio.file.std.resourcesVfs
import me.emig.engineEmi.module.EngineModule
import me.emig.engineEmi.screenElements.ScreenElement
import me.emig.engineEmi.screenElements.bodies.Ebody
import me.emig.engineEmi.screenElements.canvasElements.CanvasElement


val engine = Engine()
/**
 * Die Game-Engine. Sie ist ein Singleton und wird mit [Engine.run] gestartet.
 * @property canvasElements Alle registrieten Objekte vom Typ [CanvasElement]
 * @property bodies Alle registrieten Objekte des Typs [Ebody]
 *
 */
class Engine {
    var title = "Engine Emi"
    var module: Module = EngineModule
    var view = ViewWindow()
    var viewWillLoadBody: suspend () -> Unit = {}
    var viewDidLoadBody: suspend () -> Unit = {}

    var delay = 16.milliseconds
    var camera = Camera()

    var canvasElements = mutableListOf<CanvasElement>()
    var bodies = mutableListOf<Ebody>()
    val allScreenElements: List<ScreenElement>
        get() {
            return canvasElements.plus(bodies).map { it }
        }
    var controllers = mutableListOf<Controller>()
    var map: VfsFile? = null
    var sceneContainer: Container? = null

    fun init(initBody: () -> Unit) = this.apply {
        view.width = 1280
        view.height = 720
        view.scale = 100
        initBody()
    }

    suspend fun start() = Korge(
        Korge.Config(module = module)
    )

    fun viewWillLoad(viewWillLoadBody: suspend () -> Unit = {}) {
        this.viewWillLoadBody = viewWillLoadBody
    }

    fun viewDidLoad(viewDidLoadBody: suspend () -> Unit = {}) {
        this.viewDidLoadBody = viewDidLoadBody
    }

    /**
     * Registriert ein [CanvasElement] bei der Engine (reguläre Objekte)
     * @param canvasElement CanvasElement
     */
    fun registerCanvasElement(canvasElement: CanvasElement) {
        canvasElements.add(canvasElement)
    }

    /**
     * Registriert einen [Ebody] bei der Engine (Physikobjekte)
     * @param body Ebody
     */
    fun registerBody(body: Ebody) {
        bodies.add(body)

    }

    /**
     * Registriert einen [Controller] bei der Engine
     * @param controller Controller
     */
    fun registerController(controller: Controller) {
        controllers.add(controller)
    }

    /**
     * Registriert eine Map bei der Engine
     * @param pathToMap String zum Pfad der Tiledmap (im Resources Ordner)
     */
    fun registerMap(pathToMap: String) {
        map = resourcesVfs[pathToMap]
    }

    /**
     * Registriert einen [Ebody] oder ein [CanvasElement] bei der Engine
     * Es ist auch möglich Arrays und Collections zu registrieren.
     * [Ebody] und [CanvasElement] dürfen in den Arrays oder Collections nicht gemischt vorkommen
     * @param o Any
     */
    fun register(o: Any) {
        if (o is Array<*>)
            o.map { it?.let { register(it) } }
        if (o is Collection<*>)
            o.map { it?.let { register(it) } }
        if (o is Ebody)
            registerBody(o)
        if (o is CanvasElement)
            registerCanvasElement(o)
        if (o is Controller)
            registerController(o)
    }


}

class ViewWindow {
    var height = 0
    var width = 0
    var scale = 0
}

suspend fun WorldView.registerBodyWithWorld(body: Ebody) {
    body.initForWorld(this.world)
}