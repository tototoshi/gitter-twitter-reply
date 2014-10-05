package gittertwitterreply

import com.typesafe.scalalogging.LazyLogging

class TwitterStream (
  screenName: String,
  consumerKey: String,
  consumerSecret: String,
  accessToken: String,
  accessTokenSecret: String
) extends LazyLogging {

  import twitter4j._
  import twitter4j.conf._
  private val twitter4jConfBuilder = new ConfigurationBuilder
  private val twitter4jConf = twitter4jConfBuilder
    .setOAuthConsumerKey(consumerKey)
    .setOAuthConsumerSecret(consumerSecret)
    .setOAuthAccessToken(accessToken)
    .setOAuthAccessTokenSecret(accessTokenSecret)
    .build
  private val twitterStream = new TwitterStreamFactory(twitter4jConf).getInstance()

  def listenReply(gitter: Gitter): Unit = {
    twitterStream.addListener(new UserStreamAdapter {
      override def onStatus(status: Status): Unit = {
        val text = status.getText
        val isReplyToMe = text.contains("@" + screenName)
        val rt = Option(status.getRetweetedStatus).isDefined
        if (isReplyToMe && !rt) {
          gitter.postMessage(status.getUser.getScreenName, status.getText)
        }
      }
      override def onException(e: Exception): Unit = {
        logger.error(e.getMessage)
      }
    })
    twitterStream.user()
  }

}
