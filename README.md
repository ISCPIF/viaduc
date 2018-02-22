# ScalaWUI (Scala Web UI)#

The project aims at building a small but complete client / server application using very powerfull scala tools to construct fully typed and reactive Web applications. Among them:

- [scalajs](https://github.com/scala-js/scala-js)
- [scalatra](http://scalatra.org/)
- [scalatags](https://github.com/lihaoyi/scalatags)
- [scala.rx](https://github.com/lihaoyi/scala.rx)
- [autowire](https://github.com/lihaoyi/autowire)
- [boopickle](https://github.com/suzaku-io/boopickle)

as well as [scaladget](https://github.com/mathieuleclaire/scaladget) to draw some svg and display a [http://d3js.org/](D3.js)-like workflow.

It is an empty ready-to-work application, dealing with all the starting wiring. This prototype also exposes as example a small Graph editor inspired from [http://bl.ocks.org/cjrd/6863459](http://bl.ocks.org/cjrd/6863459) javascript example, but written witten in a reactive way thanks to the [scala.rx](https://github.com/lihaoyi/scala.rx) library.

## Build & Run##
First, build the javascript:
```sh
$ cd scalaWUI
$ sbt
> go // Build the client JS files and move them to the right place
```

Then, start the server:
```sh
> jetty:start // Start the server
```

