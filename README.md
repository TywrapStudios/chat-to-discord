# Chat To Discord; CTD
Yep, you guessed it, just another one of those simplistic mods that send your Minecraft chat to a Discord Webhook.  
But that's not all this mod does! It has a few other features that other mods like this lack.

---
### So, just for good measure, let's list all the features in this mod (in no particular order):
- Sending Player Messages directly to the Webhook.
- Sending Game Messages (e.g. Deaths, Advancements) directly to the Webhook.
- Sending a Start and Stop message for the respective server lifecycle events.
- The `/ctd` command, which displays info about your current settings and the mod!
- Alongside that, `/ctd reload` reloads your config without the need of a laggy vanilla /reload!
- Before your message is sent, it goes through some needed Compatibility handlers, which most mods lack:
  - Your message is modified to negate global pings (e.g. `@everyone`).
  - Your message is modified to negate invite links.
  - Your message is modified to negate any role pings that you don't allow in Config.
  - Your message and player names in it are modified to bypass Discord's native Markdown features (such as _Cursive text_).
  - When sharing a waypoint using Xaero's Mini-/Worldmap it displays a very "weird" message, this message can actually be deciphered using some smart Java magic and this mod rewrites your message to be more legible.
- A fully fletched way to send stuff to a Webhook, for addon Creators.
- A more robust way of handling logging, falling in line with options in Config.
- The Config gets reloaded even when the vanilla /reload command is run.
- If the server crashes, a specialized embed will be sent, displaying the main cause, and also linking to an automatically made Pastebin Site! Hence, the need to specify a Pastebin Key.
---
### A very simple Config System is also included!:
```json5
{
  "CONFIG_DO_NOT_TOUCH": "1.2", // Important internal stuff, dw about it.
  "c0": "After you've changed any of the values in this file, you can run /reload to apply changes in-game.",
  "discord_webhooks": [], // This is where the webhook links are defined. "<link>"
  "only_send_messages": false, // Whether to not send Game messages but only chat.
  "debug_mode": false, // Whether to display debug info in logs.
  "embed_mode": false, // Whether to use Embeds in rather than plain messages.
  "c1": "Please provide your own Pastebin API key from https://pastebin.com/doc_api#1",
  "pastebin_api_key": "", // A Pastebin API key, this is (for now) needed.
  "c2": "The setting below must be an RGB int, so not a `255, 255, 255` type of thing.",
  "c3": "Use this site if you want to use this feature:",
  "c4": "http://www.shodor.org/~efarrow/trunk/html/rgbint.html",
  "embed_color_rgb_int": 5489270, // Defines the main colour of the embeds.
  "c5": "A list of role ID's that users are allowed to ping from MC.",
  "role_ids": [], // A list of role IDs in form of Strings that are allowed to be pinged.
  "c6": "Set this to true if you wish not to receive LOGGER.error messages. NOT RECOMMENDED!!",
  "suppress_warns": false // Whether to suppress warn messages. (for if you get spammed)
}
```
(This code block was syntaxed with `json5` for the sake of comments, the actual file is written in `json`)
> [!NOTE]
> This file can be found at `./config/ctd.json`.
---
### Extra info:
> [!IMPORTANT]
> It is suggested to completely whitelist the Webhook for your Auto-Moderation bots.  
> Why whitelist? For starters, most bots will flag it as "spamming" since the playername is always included in the message, and thus repeated a few times, whitelisting the Webhook solves the hassle of having to remake it every time your bot deletes it.  

> [!NOTE]
> All the code and other stuff in this repository is licensed under the MIT License.  
> The only Exceptions are everything that can be found in `/main/resources/assets/ctd/*`, such as the Mod icon, this is all licensed under **ARR, All Rights Reserved Unless Explicitly stated.**  
> A separate LICENSE file can be found in there as well.
