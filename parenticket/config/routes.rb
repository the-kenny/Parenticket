Rails.application.routes.draw do

  root 'tickets#index'

  match 'projects', to: 'projects#index', via: [:options]
  match 'projects/:id', to: 'projects#show', via: [:options]
  match 'projects/:id/tickets', to: 'tickets#index', via: [:options]
  match 'projects/:project_id/tickets/:id', to: 'tickets#show', via: [:options]
  match 'tags', to: 'projects#index', via: [:options]
  match 'tags/:id', to: 'tags#show', via: [:options]

  resources :projects do
    resources :tickets
  end

  resources :tags
end
