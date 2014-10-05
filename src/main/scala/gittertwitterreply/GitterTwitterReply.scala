package gittertwitterreply

object GitterTwitterReply {

  import com.github.kxbmap.configs._
  import com.typesafe.config.ConfigFactory

  private val config = ConfigFactory.load()

  private val gitterRoomId = config.get[String]("gitter.roomId")
  private val gitterToken = config.get[String]("gitter.token")

  private val twitterScreenName = config.get[String]("twitter.screenName")
  private val twitterConsumerKey = config.get[String]("twitter.consumerKey")
  private val twitterConsumerSecret = config.get[String]("twitter.consumerSecret")
  private val twitterAccessToken = config.get[String]("twitter.accessToken")
  private val twitterAccessTokenSecret = config.get[String]("twitter.accessTokenSecret")

  def main(args: Array[String]): Unit = {
    val twitterStream = new TwitterStream(
      twitterScreenName,
      twitterConsumerKey,
      twitterConsumerSecret,
      twitterAccessToken,
      twitterAccessTokenSecret
    )

    val gitter = new Gitter(gitterRoomId, gitterToken)

    twitterStream.listenReply(gitter)
  }
}
