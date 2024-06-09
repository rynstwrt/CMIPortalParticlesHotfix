# CMIPortalParticlesHotfix
A Spigot plugin to fix CMI portal particles resetting!

<br>

## TUTORIAL
On first run, the plugin will create a CMIPortalParticlesHotfix folder in your plugins directory.

It will create a `config.yml` file and a `portals.yml` file that will have entries for all of your CMI portals and their used particle type and particle amount.

These will be not what you want, so edit the portals.yml file to set what particles you want.

Then run `/portalfix reload` and `/portalfix fix` to change the CMI portals to use the particles you specified in portals.yml!

<br>

## FEATURES
- Portals fix themselves after `/cmi reload` (from either a player or the console)
- Runs the portal fix on server start.
- Includes a way to manually run the portal fix if it doesn't work automatically for some reason.

<br>

## COMMANDS/PERMISSIONS
- `/cmiportalparticleshotfix <help/fix/reload>`
  - Aliases: [`/particlehotfix`, `/fixportals`, `/portalfix`]
  - Permission node: `cmiportalparticlehotfix.fixportals`

<br>

## CONFIG.YML
```yml
# Command prefix
prefix: "&2CMIPortalHotfix &7»"

# Should the fix be run on server start?
fix-on-start: true

# How long (IN TICKS) after a player runs /cmi reload should the portals be fixed?
# It uses PlayerCommandPreprocessEvent, so you have to estimate how long
# the CMI reload will take. 20-40 ticks seems to work for most cases.
player-reload-fix-delay: 30
```

<br>

## EXAMPLE PORTALS.YML
The default portal particle amount in-game shows 20, but is stored as 1.
```yml
portals:
  creative:
    particle: Sculk Soul
    amount: 1
  skyblock:
    particle: Sculk Soul
    amount: 1
  survival:
    particle: Sculk Soul
    amount: 1

```
