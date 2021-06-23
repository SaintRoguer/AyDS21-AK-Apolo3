# AK-Hera2-NYTimesData
Repositorio para la abstracción del repository external

- ¿Para que esta destinado?
 
  Esta desarrollado para obtener articulos de NYTimes. 
  Más especifico:
    - Descripción, en concreto el abstract del articulo que brinda NYTimes 
    - URL del articulo en concreto
    - Logo de NYTimes
    
- ¿Como conseguir el articulo?
  - 1 -> Agregar como submodulo al proyecto.
  - 2 -> Luego haciendo referencia a getArticleInfo(artistName) de la interfaz NYTimesArticleService, donde artistName es el nombre del artista deseado.
  - 3 -> Se retornara un Article, que debera ser parseado a la entidad Card.
  
- Casos extremos
  - En el caso de no tener conexión a internet se devolvera "null"
  - En el caso de que no exista articulo se devolvera "null" también

- A continuacion mostramos un ejemplo de como y el resultado que se obtendra:


  import ayds.jkhera2.nytimes.NYTArticleService

  import ayds.jkhera2.nytimes.entities.Article

  private val nytArticleService: NYTArticleService = NYTModule.nytArticleService

  val article = nytArticleService.getArtistInfo("The Weeknd")

  print(article.description)

  print(article.infoUrl)

  print(article.sourceLogoUrl)

<br/>

- Los resultados a estas tres consultas son:

"Since 1989, small groups have whittled down 61 of the awards’ 84 categories. The Weeknd, who criticized the process, applauded the change but said he would not lift his boycott

https://www.nytimes.com/2021/04/30/arts/music/grammys-secret-committees.html

https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVioI832nuYIXqzySD8cOXRZEcdlAj3KfxA62UEC4FhrHVe0f7oZXp3_mSFG7nIcUKhg&usqp=CAU
