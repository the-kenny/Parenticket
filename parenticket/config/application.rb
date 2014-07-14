require File.expand_path('../boot', __FILE__)

require 'rails/all'

Bundler.require(*Rails.groups)

module Parenticket
  class Application < Rails::Application
    config.action_dispatch.default_headers.merge!({
                                                    'Access-Control-Allow-Origin' => '*',
                                                    'Access-Control-Request-Method' => '*',
                                                    'Access-Control-Allow-Methods' => 'POST, PUT, DELETE, GET, OPTIONS',
                                                    'Access-Control-Allow-Headers' => 'Origin, X-Requested-With, Content-Type, Accept, Authorization'
                                                  })
  end
end
