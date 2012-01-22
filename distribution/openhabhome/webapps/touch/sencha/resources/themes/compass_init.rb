# This file registers the sencha-touch framework with compass
# It's a magic name that compass knows how to find.
dir = File.dirname(__FILE__)
require File.join(dir, 'lib', 'theme_images.rb')

Compass::Frameworks.register 'sencha-touch', dir
