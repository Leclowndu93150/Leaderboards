# 2.0.3
- The singleplayer button-hiding from 2.0.2 is now opt-in. New client config option `hideButtonWhenOnlyOnePlayerExist` (off by default, in `config/leaderboards-client.json`); when enabled, the sidebar button hides on singleplayer worlds with only one player in playerdata.

# 2.0.2
- Hide the sidebar leaderboard button in true singleplayer. It now shows when connected to a remote server, or when the integrated world's playerdata contains more than one player (LAN, e4mc, etc.).