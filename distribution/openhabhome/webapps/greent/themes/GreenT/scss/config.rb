# Get the directory that this configuration file exists in
dir = File.dirname(__FILE__)

sencha_dir='sencha-2.4.2'

fonts_path =  File.join(dir, '..', '..', '..', sencha_dir, 'resources', 'themes', 'fonts')

# Load the sencha-touch framework automatically.
load File.join(dir, '..', '..', '..', sencha_dir, 'resources', 'themes')

# Compass configurations
sass_path    = dir
css_path     = File.join(dir, "..", "css")
environment  = :production
output_style = :compressed