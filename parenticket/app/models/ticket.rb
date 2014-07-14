class Ticket < ActiveRecord::Base
  include ActiveModel::Serialization

  belongs_to :project
  has_and_belongs_to_many :tags

  def serializable_hash(options = {})
    foo = super(options)
    foo["tags"] = tags.map { |tag| tag.name }
    foo
  end
end
