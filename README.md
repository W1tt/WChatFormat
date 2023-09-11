# WChatFormat
Simple chat format plugin for PaperMC servers, currently not compatible with PlaceholderAPI

#Requirements
Currently this plugin does not require any external library or another plugin to run correctly, but in future it's going to depend on PlaceholderAPI
#Config
```
msg-format-sender: '&7(&eJa &7→ {PREFIX}{DISPLAYNAME}{SUFFIX}&7) &8» &r{MESSAGE}' 
msg-format-viewer: '&7({PREFIX}{DISPLAYNAME}{SUFFIX} &7→ &eJa&7) &8» &r{MESSAGE}'
format: '{PREFIX}{DISPLAYNAME}{SUFFIX}&8: {MESSAGE}'
msg-on-click: true
player-hover:
  enabled: true
  format:
  - '&7Nick: &e{DISPLAYNAME}'
  - '&7Na serwerze od: &e{LOGINDATE}'
  - ''
  - '&7Kliknij by napisać do tego gracza!'

```
- msg-format-sender - The message that the player that sends a private message will receive
- msg-format-viewer - The message that the player that the message is sent to will receive
- format - It's just chat message format
- msg-on-click - True if you want to suggest /msg command after clicking on player's displayname in chat
- player-hover - The text that appears after hovering over the player's displayname in chat

#To-do list
- PlaceholderAPI support
- Vanish support
- Ignore command, or Essentials ignore command support (if installed)
